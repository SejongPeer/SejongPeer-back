package com.sejong.sejongpeer.domain.honbab.service;

import java.time.Duration;
import java.time.LocalDateTime;
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
import com.sejong.sejongpeer.global.util.MemberUtil;
import com.sejong.sejongpeer.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HonbabService {
	private final HonbabRepository honbabRepository;
	private final MemberRepository memberRepository;
	private final HonbabMatchedRepository honbabMatchedRepository;
	private final HonbabMatchingService honbabMatchingService;
	private final MemberUtil memberUtil;
	private final SecurityUtil securityUtil;

	public void registerHonbab(RegisterHonbabRequest request) {
		final String memberId = securityUtil.getCurrentMemberId();
		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		checkPossibleRegistration(memberId);

		Honbab honbab = Honbab.createHonbab(member, request);
		honbabRepository.save(honbab);

		honbabMatchingService.matchHonbabWhenRegister(honbab);
	}

	@Transactional(readOnly = true)
	public HonbabMatchingStatusResponse getHonbabMatchingStatus() {
		final String memberId = securityUtil.getCurrentMemberId();

		Optional<Honbab> optionalHonbab = getLastestHonbabByMemberId(memberId);

		if (optionalHonbab.isPresent()) {
			Honbab honbab = optionalHonbab.get();

			if (honbab.getStatus() == HonbabStatus.MATCHING_COMPLETED &&
				Duration.between(honbab.getUpdatedAt(), LocalDateTime.now()).toMinutes() > 15) {
				honbab.changeStatus(HonbabStatus.EXPIRED);
			}
			return HonbabMatchingStatusResponse.from(honbab);
		} else {
			return null;
		}
	}

	public MatchingPartnerInfoResponse getPartnerInfo() {
		final String memberId = securityUtil.getCurrentMemberId();

		Honbab lastestHonbab = getLastestHonbabByMemberId(memberId).orElseThrow(
			() -> new CustomException(ErrorCode.HONBAB_NOT_FOUND));
		HonbabMatched selectedHonbabMatched = getLastestHonbabMatchedByHonbab(lastestHonbab).orElseThrow(
			() -> new CustomException(ErrorCode.TARGET_HONBAB_NOT_FOUND));
		Honbab targetHonbab = getHonbabFriend(selectedHonbabMatched, lastestHonbab);
		Member targetHonbabMember = targetHonbab.getMember();

		return MatchingPartnerInfoResponse.of(targetHonbabMember, targetHonbab);
	}

	public ActiveCustomersCountResponse getCurrentlyActiveHonbabCount() {
		Long activeHonbabCount = honbabRepository.countByStatusInProgressHonbab();
		return ActiveCustomersCountResponse.of(activeHonbabCount);
	}

	public void cancelHonbab() {
		final String memberId = securityUtil.getCurrentMemberId();

		Honbab latestHonbab = getLastestHonbabByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.HONBAB_NOT_FOUND));

		ensureInProgressStatus(latestHonbab);
		latestHonbab.changeStatus(HonbabStatus.CANCEL);
		honbabRepository.save(latestHonbab);
	}

	private void validateInProgressStatus(Honbab honbab) {
		if (honbab.getStatus() == HonbabStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.REGISTRATION_NOT_POSSIBLE);
		}
	}

	private void ensureInProgressStatus(Honbab honbab) {
		if (honbab.getStatus() != HonbabStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
	}

	private void checkIfRegistrationTimeHasPassed(Honbab honbab) {
		if (honbab.getStatus() == HonbabStatus.MATCHING_COMPLETED &&
			isReRegistrationTimePassed(honbab)) {
			throw new CustomException(ErrorCode.HONBAB_REGISTRATION_LIMIT);
		}
	}

	private boolean isReRegistrationTimePassed(Honbab honbab) {
		return (Duration.between(honbab.getUpdatedAt(), LocalDateTime.now()).toMinutes() < 15);
	}

	private void checkPossibleRegistration(String memberId) {
		Honbab lastHonbab = getLastHonbabByMemberId(memberId)
			.orElse(null);

		if (lastHonbab == null) {
			return;
		}
		validateInProgressStatus(lastHonbab);
		checkIfRegistrationTimeHasPassed(lastHonbab);
	}

	private Optional<Honbab> getLastHonbabByMemberId(String memberId) {
		return honbabRepository.findLastHonbabByMemberId(memberId);
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
}


