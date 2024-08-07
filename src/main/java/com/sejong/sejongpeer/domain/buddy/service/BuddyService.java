package com.sejong.sejongpeer.domain.buddy.service;

import com.sejong.sejongpeer.domain.buddy.dto.request.BuddyRegistrationRequest;
import com.sejong.sejongpeer.domain.buddy.dto.response.ActiveCustomersCountResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.CompletedPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingStatusResponse;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;
import com.sejong.sejongpeer.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BuddyService {
	private static final int MAX_MATCHING_COMPLETED_BUDDIES = 3;
	private static final int MATCH_BLOCK_HOUR = 1;

	private final MatchingService matchingService;
	private final BuddyRepository buddyRepository;
	private final BuddyMatchingService buddyMatchingService;
	private final MemberUtil memberUtil;
	private final SecurityUtil securityUtil;

	public void registerBuddy(BuddyRegistrationRequest request) {
		final Member member = memberUtil.getCurrentMember();

		validatePossibleRegistration(member.getId());

		Buddy buddy = Buddy.create(request, member);
		buddyRepository.save(buddy);

		matchingService.matchBuddyWhenRegister(buddy);
	}

	public void cancelBuddy() {
		final String memberId = securityUtil.getCurrentMemberId();
		Buddy latestBuddy = buddyRepository.findLastBuddyByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		ensureBuddyStatusMatches(latestBuddy, BuddyStatus.IN_PROGRESS, ErrorCode.NOT_IN_PROGRESS);

		latestBuddy.changeStatus(BuddyStatus.CANCEL);
		buddyRepository.save(latestBuddy);
	}

	private void validatePossibleRegistration(String memberId) {
		Optional<Buddy> optionalBuddy = buddyRepository.findLastBuddyByMemberId(memberId);

		optionalBuddy.ifPresent(latestBuddy -> {
			validateBuddyStatusInProgress(latestBuddy);
			validateRejectPenaltyAndUpdateStatus(latestBuddy);
			validateBuddyRegistrationsCount(memberId);
		});
	}

	private void validateRejectPenaltyAndUpdateStatus(Buddy buddy) {
		if (buddy.getStatus().equals(BuddyStatus.REJECT) &&
			!isPossibleFromUpdateAt(buddy.getUpdatedAt())) {
			throw new CustomException(ErrorCode.REJECT_PENALTY);
		}
	}

	private void validateBuddyRegistrationsCount(String memberId) {
		if (countMatchingCompletedBuddies(memberId) >= MAX_MATCHING_COMPLETED_BUDDIES) {
			throw new CustomException(ErrorCode.MAX_BUDDY_REGISTRATION_EXCEEDED);
		}
	}

	private boolean isPossibleFromUpdateAt(LocalDateTime updatedAt) {
		LocalDateTime oneHourAfterTime = updatedAt.plusHours(MATCH_BLOCK_HOUR);
		return LocalDateTime.now().isAfter(oneHourAfterTime);
	}

	public MatchingStatusResponse getBuddyMatchingStatusAndCount() {
		final String memberId = securityUtil.getCurrentMemberId();

		Buddy lastBuddy = buddyRepository.findLastBuddyByMemberId(memberId)
			.orElse(null);

		if (lastBuddy == null) {
			return null;
		}

		checkAndUpdateRejectedBuddyStatus(lastBuddy);
		Long matchingCompletedCount = buddyRepository.countByMemberIdAndStatus(memberId,
			BuddyStatus.MATCHING_COMPLETED);
		return MatchingStatusResponse.of(lastBuddy, matchingCompletedCount);
	}

	private void checkAndUpdateRejectedBuddyStatus(Buddy buddy) {
		if (buddy.getStatus().equals(BuddyStatus.REJECT) &&
			isPossibleFromUpdateAt(buddy.getUpdatedAt())) {
			buddy.changeStatus(BuddyStatus.REACTIVATE);
			buddyRepository.save(buddy);
		}
	}

	public MatchingPartnerInfoResponse getBuddyMatchingPartnerDetails() {
		final String memberId = securityUtil.getCurrentMemberId();

		Buddy latestBuddy = buddyRepository.findLastBuddyByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));
		ensureBuddyStatusMatches(latestBuddy, BuddyStatus.FOUND_BUDDY, ErrorCode.BUDDY_NOT_MATCHED);

		BuddyMatched latestBuddyMatched = buddyMatchingService.getLatestBuddyMatched(latestBuddy);
		checkBuddyMatchedStatus(latestBuddyMatched);

		Buddy partner = buddyMatchingService.getOtherBuddyInBuddyMatched(latestBuddyMatched, latestBuddy);
		Member partnerMember = partner.getMember();

		return MatchingPartnerInfoResponse.memberFrom(partnerMember);
	}

	public List<CompletedPartnerInfoResponse> getBuddyMatchedPartnerDetails() {
		final String memberId = securityUtil.getCurrentMemberId();

		List<Buddy> completedBuddies = buddyRepository.findAllByMemberIdAndStatus(memberId,
			BuddyStatus.MATCHING_COMPLETED);
		checkCompletedBuddies(completedBuddies);

		return completedBuddies.stream()
			.map(buddy -> {
				BuddyMatched completedBuddyMatched = buddyMatchingService.getBuddyMatchedByBuddy(buddy);
				Buddy partner = buddyMatchingService.getOtherBuddyInBuddyMatched(completedBuddyMatched, buddy);
				Member partnerMember = partner.getMember();
				return CompletedPartnerInfoResponse.from(partnerMember);
			})
			.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));

	}

	private void checkBuddyMatchedStatus(BuddyMatched buddyMatched) {
		if (buddyMatched.getStatus() != BuddyMatchedStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
	}

	private void checkCompletedBuddies(List<Buddy> completedBuddies) {
		if (completedBuddies.isEmpty())
			throw new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND);
	}

	public ActiveCustomersCountResponse getCurrentlyActiveBuddyCount() {
		List<BuddyStatus> statuses = List.of(BuddyStatus.IN_PROGRESS, BuddyStatus.FOUND_BUDDY);
		Long activeBuddyCount = buddyRepository.countByStatusIn(statuses);

		return ActiveCustomersCountResponse.of(activeBuddyCount);
	}

	private long countMatchingCompletedBuddies(String memberId) {
		return buddyRepository.countByMemberIdAndStatus(memberId, BuddyStatus.MATCHING_COMPLETED);
	}

	private void validateBuddyStatusInProgress(Buddy buddy) {
		if (buddy.getStatus() == BuddyStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.REGISTRATION_NOT_POSSIBLE);
		}
	}

	private void ensureBuddyStatusMatches(Buddy buddy, BuddyStatus buddyStatus, ErrorCode errorCode) {
		if (buddy.getStatus() != buddyStatus) {
			throw new CustomException(errorCode);
		}
	}
}
