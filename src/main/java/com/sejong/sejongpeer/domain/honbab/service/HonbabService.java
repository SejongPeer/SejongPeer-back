package com.sejong.sejongpeer.domain.honbab.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.buddy.dto.response.ActiveCustomersCountResponse;
import com.sejong.sejongpeer.domain.honbab.dto.request.RegisterHonbabRequest;
import com.sejong.sejongpeer.domain.honbab.dto.response.HonbabMatchingStatusResponse;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.domain.honbab.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.HonbabMatched;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabMatchedRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HonbabService {
	private final HonbabRepository honbabRepository;
	private final MemberRepository memberRepository;
	private final HonbabMatchedRepository honbabMatchedRepository;
	private final HonbabMatchingService honbabMatchingService;

	public void registerHonbab(RegisterHonbabRequest request, String memberId) {
		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		checkPossibleRegistration(memberId);

		Honbab honbab = Honbab.createHonbab(member, request);
		honbabRepository.save(honbab);

		honbabMatchingService.matchHonbabWhenRegister(honbab);
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
	public HonbabMatchingStatusResponse getHonbabMatchingStatus(String memberId) {
		Optional<Honbab> optionalHonbab = getLastHonbabByMemberId(memberId);

		if (optionalHonbab.isPresent()) {
			Honbab honbab = optionalHonbab.get();
			return HonbabMatchingStatusResponse.honbabFrom(honbab);
		} else {
			return null;
		}
	}

	public MatchingPartnerInfoResponse getPartnerInfo(String memberId) {

		Honbab lastestHonbab = getLastestHonbabByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.HONBAB_NOT_FOUND));
		HonbabMatched selectedHonbabMatched = getLastestHonbabMatchedByHonbab(lastestHonbab).orElseThrow(() -> new CustomException(ErrorCode.TARGET_HONBAB_NOT_FOUND));
		Honbab targetHonbab = getHonbabFriend(selectedHonbabMatched, lastestHonbab);
		Member targetHonbabMember = targetHonbab.getMember();

		return MatchingPartnerInfoResponse.of(targetHonbabMember, targetHonbab);

	}

	private Optional<Honbab> getLastestHonbabByMemberId(String memberId) {
		return honbabRepository.findLastHonbabByMemberId(memberId);
	}

	private Optional<HonbabMatched> getLastestHonbabMatchedByHonbab(Honbab applicant) {
		// 만들어진 HonbabMatched의 status는 항상 MATCHING_COMPLETED
		return honbabMatchedRepository.findLatestByOwnerOrPartner(applicant);
	}

	private Honbab getHonbabFriend(HonbabMatched selectedMatched, Honbab applicant) {
		if (selectedMatched.getOwner() == applicant) {
			return selectedMatched.getPartner();
		}
		return selectedMatched.getOwner();
	}

	public ActiveCustomersCountResponse getCurrentlyActiveHonbabCount() {
		Long activeHonbabCount = honbabRepository.countByStatusInProgressHonbab();
		return new ActiveCustomersCountResponse(activeHonbabCount);
  }
  
	public void cancelHonbab(String memberId) {
		Honbab latestHonbab = getLastestHonbabByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.HONBAB_NOT_FOUND));

		if (latestHonbab.getStatus() != HonbabStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
		latestHonbab.changeStatus(HonbabStatus.CANCEL);
		honbabRepository.save(latestHonbab);
	}
}


