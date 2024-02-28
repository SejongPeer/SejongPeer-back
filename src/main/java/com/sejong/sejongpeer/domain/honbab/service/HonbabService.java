package com.sejong.sejongpeer.domain.honbab.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingStatusResponse;
import com.sejong.sejongpeer.domain.honbab.dto.request.RegisterHonbabRequest;
import com.sejong.sejongpeer.domain.honbab.dto.response.HonbabPartnerInfoResponse;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;
import com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.HonbabMatched;
import com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.type.HonbabMatchedStatus;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabMatchedRepository;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HonbabService {
	private final HonbabRepository honbabRepository;
	private final MemberRepository memberRepository;
	private final HonbabMatchedRepository honbabMatchedRepository;

	public void registerHonbab(RegisterHonbabRequest request, String memberId) {
		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		checkPossibleRegistration(memberId);

		Honbab honbab = createHonbabEntity(request, member);
		honbabRepository.save(honbab);
	}

	private Honbab createHonbabEntity(RegisterHonbabRequest createHonbabRequest, Member member) {
		return Honbab.createHonbab(
			member,
			HonbabStatus.IN_PROGRESS,
			createHonbabRequest.genderOption(),
			createHonbabRequest.menuCategoryOption()
		);
	}
	private void checkPossibleRegistration(String memberId) {
		Optional<Honbab> optionalHonbab = getLastHonbabByMemberId(memberId);

		optionalHonbab.ifPresent(latestHonbab -> {
			if (latestHonbab.getStatus() == HonbabStatus.IN_PROGRESS) {
				throw new CustomException(ErrorCode.REGISTRATION_NOT_POSSIBLE);
			}
		});
	}

	private Optional<Honbab> getLastHonbabByMemberId(String memberId) {
		return honbabRepository.findLastHonbabByMemberId(memberId);
	}

	@Transactional(readOnly = true)
	public MatchingStatusResponse getHonbabMatchingStatus(String memberId) {
		Optional<Honbab> optionalHonbab = getLastHonbabByMemberId(memberId);
		Honbab honbab = optionalHonbab.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		return new MatchingStatusResponse(honbab.getStatus().toString());
	}

	public HonbabPartnerInfoResponse getHonbabMPartnerDetails(String memberId) {

		Honbab latestHonbab = getLastHonbabByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.HONBAB_NOT_FOUND));

		checkHonbabStatus(latestHonbab, HonbabStatus.MATCHING_COMPLETED);

		HonbabMatched latestHonbabMatched = getLatestHonbabMatched(latestHonbab);
		checkHonbabMatchedStatus(latestHonbabMatched, HonbabMatchedStatus.MATCHING_COMPLETED);

		Honbab partner = getOtherHonbabInHonbabMatched(latestHonbabMatched, latestHonbab);
		Member partnerMember = partner.getMember();

		return (new HonbabPartnerInfoResponse(
			partnerMember.getName(),
			partnerMember.getKakaoAccount(),
			partner.getMenuCategoryOption().toString()
		));
	}

	private HonbabMatched getLatestHonbabMatched(Honbab honbab) {
		Optional<HonbabMatched> optionalHonbabMatched = honbabMatchedRepository.findLatestByOwnerOrPartner(honbab);

		return  (optionalHonbabMatched.orElseThrow(() -> new CustomException(ErrorCode.TARGET_HONBAB_NOT_FOUND)));
	}

	private void checkHonbabStatus(Honbab honbab, HonbabStatus status) {
		if (honbab.getStatus() != status) {
			throw new CustomException(ErrorCode.HONBAB_NOT_MATCHED);
		}
	}

	private void checkHonbabMatchedStatus(HonbabMatched honbabMatched, HonbabMatchedStatus status) {
		if (honbabMatched.getStatus() != status) {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
	}

	private Honbab getOtherHonbabInHonbabMatched(HonbabMatched honbabMatched, Honbab ownerHonbab) {
		if (honbabMatched.getOwner() == ownerHonbab) {
			return honbabMatched.getPartner();
		} else {
			return honbabMatched.getOwner();
		}
	}
}
