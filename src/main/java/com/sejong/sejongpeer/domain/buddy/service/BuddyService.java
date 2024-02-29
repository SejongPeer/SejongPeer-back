package com.sejong.sejongpeer.domain.buddy.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.buddy.constant.LimitTimeConstant;
import com.sejong.sejongpeer.domain.buddy.dto.request.RegisterRequest;
import com.sejong.sejongpeer.domain.buddy.dto.response.CompletedPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingStatusResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class BuddyService {
	private final MatchingService matchingService;
	private final BuddyRepository buddyRepository;
	private final MemberRepository memberRepository;
	private final BuddyMatchingService buddyMatchingService;

	public void registerBuddy(RegisterRequest request, String memberId) {
		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		checkPossibleRegistration(memberId);

		Buddy buddy = createBuddyEntity(request, member);
		buddyRepository.save(buddy);

		matchingService.matchBuddyWhenRegister(buddy);
	}

	private Buddy createBuddyEntity(RegisterRequest createBuddyRequest, Member member) {
		return Buddy.createBuddy(
			member,
			createBuddyRequest.genderOption(),
			createBuddyRequest.classTypeOption(),
			createBuddyRequest.collegeMajorOption(),
			createBuddyRequest.gradeOption(),
			BuddyStatus.IN_PROGRESS,
			createBuddyRequest.isSubMajor()
		);
	}

	public void cancelBuddy(String memberId) {
		Buddy latestBuddy = getLastBuddyByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		if (latestBuddy.getStatus() != BuddyStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
		latestBuddy.changeStatus(BuddyStatus.CANCEL);
		buddyRepository.save(latestBuddy);
	}

	private void checkPossibleRegistration(String memberId) {
		Optional<Buddy> optionalBuddy = getLastBuddyByMemberId(memberId);

		optionalBuddy.ifPresent(latestBuddy -> {
			checkInProgressStatus(latestBuddy);
			checkRejectPenaltyAndUpdateStatus(latestBuddy);
		});
	}

	private void checkInProgressStatus(Buddy buddy) {
		if (buddy.getStatus() == BuddyStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.REGISTRATION_NOT_POSSIBLE);
		}
	}

	private void checkRejectPenaltyAndUpdateStatus(Buddy buddy) {
		if (buddy.getStatus().equals(BuddyStatus.REJECT) &&
			!isPossibleFromUpdateAt(buddy.getUpdatedAt())) {
			throw new CustomException(ErrorCode.REJECT_PENALTY);
		}
	}

	private Optional<Buddy> getLastBuddyByMemberId(String memberId) {
		return buddyRepository.findLastBuddyByMemberId(memberId);
	}

	private boolean isPossibleFromUpdateAt(LocalDateTime updatedAt) {
		LocalDateTime oneHourAfterTime = updatedAt.plusHours(LimitTimeConstant.MATCH_BLOCK_HOUR);

		return LocalDateTime.now().isAfter(oneHourAfterTime);
	}

	@Transactional(readOnly = true)
	public MatchingStatusResponse getMatchingStatus(String memberId) {
		Optional<Buddy> optionalBuddy = getLastBuddyByMemberId(memberId);
		Buddy buddy = optionalBuddy.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		return new MatchingStatusResponse(buddy.getStatus());
	}

	public MatchingPartnerInfoResponse getPartnerDetails(String memberId) {

		Buddy latestBuddy = getLastBuddyByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));
		checkBuddyStatus(latestBuddy, BuddyStatus.FOUND_BUDDY);

		BuddyMatched latestBuddyMatched = buddyMatchingService.getLatestBuddyMatched(latestBuddy);
		checkBuddyMatchedStatus(latestBuddyMatched, BuddyMatchedStatus.IN_PROGRESS);

		Buddy partner =  buddyMatchingService.getOtherBuddyInBuddyMatched(latestBuddyMatched, latestBuddy);
		Member partnerMember = partner.getMember();

		return MatchingPartnerInfoResponse.memberFrom(partnerMember);
	}

	public CompletedPartnerInfoResponse getBuddyMatchedPartnerDetails(String memberId) {

		Buddy latestBuddy = getLastBuddyByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));
		checkBuddyStatus(latestBuddy, BuddyStatus.MATCHING_COMPLETED);

		BuddyMatched latestBuddyMatched = buddyMatchingService.getLatestBuddyMatched(latestBuddy);
		checkBuddyMatchedStatus(latestBuddyMatched, BuddyMatchedStatus.MATCHING_COMPLETED);

		Buddy partner =  buddyMatchingService.getOtherBuddyInBuddyMatched(latestBuddyMatched, latestBuddy);
		Member partnerMember = partner.getMember();

		return CompletedPartnerInfoResponse.memberFrom(partnerMember);
	}

	private void checkBuddyStatus(Buddy buddy, BuddyStatus status) {
		if (buddy.getStatus() != status) {
			throw new CustomException(ErrorCode.BUDDY_NOT_MATCHED);
		}
	}

	private void checkBuddyMatchedStatus(BuddyMatched buddyMatched, BuddyMatchedStatus status) {
		if (buddyMatched.getStatus() != status) {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
	}
}
