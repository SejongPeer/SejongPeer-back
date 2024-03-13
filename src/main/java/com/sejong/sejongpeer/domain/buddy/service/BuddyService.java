package com.sejong.sejongpeer.domain.buddy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.List;
import java.util.Optional;

import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.buddy.constant.LimitTimeConstant;
import com.sejong.sejongpeer.domain.buddy.dto.request.RegisterRequest;
import com.sejong.sejongpeer.domain.buddy.dto.response.CompletedPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingStatusResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.ActiveCustomersCountResponse;
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
	private static final int MAX_MATCHING_COMPLETED_BUDDIES = 3;

	private final MatchingService matchingService;
	private final BuddyMatchedRepository buddyMatchedRepository;
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
		Buddy latestBuddy = buddyRepository.findLastBuddyByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		if (latestBuddy.getStatus() != BuddyStatus.IN_PROGRESS) {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
		latestBuddy.changeStatus(BuddyStatus.CANCEL);
		buddyRepository.save(latestBuddy);
	}

	private void checkPossibleRegistration(String memberId) {
		Optional<Buddy> optionalBuddy = buddyRepository.findLastBuddyByMemberId(memberId);

		optionalBuddy.ifPresent(latestBuddy -> {
			checkInProgressStatus(latestBuddy);
			checkRejectPenaltyAndUpdateStatus(latestBuddy);
		});
		checkCountBuddyRegistrations(memberId);
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

	private void checkCountBuddyRegistrations(String memberId) {
		if (countMatchingCompletedBuddies(memberId) >= MAX_MATCHING_COMPLETED_BUDDIES) {
			throw new CustomException(ErrorCode.MAX_BUDDY_REGISTRATION_EXCEEDED);
		}
	}

	private boolean isPossibleFromUpdateAt(LocalDateTime updatedAt) {
		LocalDateTime oneHourAfterTime = updatedAt.plusHours(LimitTimeConstant.MATCH_BLOCK_HOUR);
		return LocalDateTime.now().isAfter(oneHourAfterTime);
	}

	@Transactional(readOnly = true)
	public MatchingStatusResponse getMatchingStatus(String memberId) {
		Optional<Buddy> optionalBuddy = buddyRepository.findLastBuddyByMemberId(memberId);

		if (optionalBuddy.isPresent()) {
			Buddy buddy = optionalBuddy.get();
			return MatchingStatusResponse.buddyFrom(buddy);
		} else {
			return null;
		}
	}

	public MatchingPartnerInfoResponse getPartnerDetails(String memberId) {

		Buddy latestBuddy = buddyRepository.findLastBuddyByMemberId(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));
		checkBuddyStatus(latestBuddy, BuddyStatus.FOUND_BUDDY);

		BuddyMatched latestBuddyMatched = buddyMatchingService.getLatestBuddyMatched(latestBuddy);
		checkBuddyMatchedStatus(latestBuddyMatched, BuddyMatchedStatus.IN_PROGRESS);

		Buddy partner = buddyMatchingService.getOtherBuddyInBuddyMatched(latestBuddyMatched, latestBuddy);
		Member partnerMember = partner.getMember();

		return MatchingPartnerInfoResponse.memberFrom(partnerMember);
	}

	public List<CompletedPartnerInfoResponse> getBuddyMatchedPartnerDetails(String memberId) {

		List<Buddy> completedBuddies = buddyRepository.findAllByMemberIdAndStatus(memberId, BuddyStatus.MATCHING_COMPLETED);
		checkCompletedBuddies(completedBuddies);

		return completedBuddies.stream()
			.map(buddy -> {
				BuddyMatched completedBuddyMatched = buddyMatchedRepository.findByOwnerOrPartner(buddy)
					.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_MATCHED));

				Buddy partner = buddyMatchingService.getOtherBuddyInBuddyMatched(completedBuddyMatched, buddy);
				Member partnerMember = partner.getMember();
				return CompletedPartnerInfoResponse.memberFrom(partnerMember);
			})
			.toList();
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

	private void checkCompletedBuddies(List<Buddy> completedBuddies) {
		if (completedBuddies.isEmpty())
			throw new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND);
	}

	public ActiveCustomersCountResponse getCurrentlyActiveBuddyCount() {
		List<BuddyStatus> statuses = List.of(BuddyStatus.IN_PROGRESS, BuddyStatus.FOUND_BUDDY);
		Long activeBuddyCount = buddyRepository.countByStatusIn(statuses);

		return new ActiveCustomersCountResponse(activeBuddyCount);
	}

	private long countMatchingCompletedBuddies(String memberId) {
		return buddyRepository.countByMemberIdAndStatus(memberId, BuddyStatus.MATCHING_COMPLETED);
	}
}
