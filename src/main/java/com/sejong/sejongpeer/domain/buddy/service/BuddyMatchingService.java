package com.sejong.sejongpeer.domain.buddy.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sejong.sejongpeer.domain.buddy.dto.request.MatchingResultRequest;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepository;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepositoryImpl;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.infra.sms.service.SmsService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BuddyMatchingService {

	private final BuddyMatchedRepository buddyMatchedRepository;
	private final BuddyMatchedRepositoryImpl buddyMatchedRepositoryImpl;
	private final BuddyRepository buddyRepository;
	private final SmsService smsService;

	public void updateBuddyMatchingStatus(String memberId, MatchingResultRequest request) {

		Buddy ownerLatestBuddy = buddyRepository.findTopByMemberIdAndStatusOrderByCreatedAtDesc(memberId, BuddyStatus.FOUND_BUDDY)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		if (!request.isAccept()) {
			ownerLatestBuddy.changeStatus(BuddyStatus.REJECT);
		} else {
			ownerLatestBuddy.changeStatus(BuddyStatus.ACCEPT);
		}

		BuddyMatched progressMatch = getBuddyMatchedByBuddy(ownerLatestBuddy);

		Buddy partnerBuddy = getOtherBuddyInBuddyMatched(progressMatch, ownerLatestBuddy);

		updateStatusBasedOnBuddies(progressMatch, ownerLatestBuddy, partnerBuddy);

		buddyMatchedRepository.save(progressMatch);
	}


	private void updateStatusBasedOnBuddies(BuddyMatched buddyMatched, Buddy ownerBuddy, Buddy targetBuddy) {

		if (ownerBuddy.getStatus() == BuddyStatus.REJECT) {
			handleBuddyMatchedReject(buddyMatched, ownerBuddy, targetBuddy);
			return;
		}

		if (ownerBuddy.getStatus() == BuddyStatus.ACCEPT && targetBuddy.getStatus() == BuddyStatus.FOUND_BUDDY) {
			return;
		}

		if (ownerBuddy.getStatus() == BuddyStatus.ACCEPT && targetBuddy.getStatus() == BuddyStatus.ACCEPT) {
			handleBuddyMatchedSuccess(buddyMatched, ownerBuddy, targetBuddy);
		}
	}

	private void handleBuddyMatchedReject(BuddyMatched buddyMatched, Buddy ownerBuddy, Buddy targetBuddy) {
		buddyMatched.changeStatus(BuddyMatchedStatus.MATCHING_FAIL);
		ownerBuddy.changeStatus(BuddyStatus.REJECT);
		targetBuddy.changeStatus(BuddyStatus.DENIED);

		sendMatchingFailurePenaltyMessage(ownerBuddy, SmsText.MATCHING_FAILED);
		sendMatchingFailurePenaltyMessage(targetBuddy, SmsText.MATCHING_FAILED);
	}

	private void handleBuddyMatchedSuccess(BuddyMatched buddyMatched, Buddy ownerBuddy, Buddy targetBuddy) {
		buddyMatched.changeStatus(BuddyMatchedStatus.MATCHING_COMPLETED);
		ownerBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);
		targetBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);

		sendMatchingSuccessMessage(ownerBuddy);
		sendMatchingSuccessMessage(targetBuddy);
	}

	public BuddyMatched getLatestBuddyMatched(Buddy buddy) {
		Optional<BuddyMatched> optionalBuddyMatched = buddyMatchedRepository.findLatestByOwnerOrPartner(buddy);
		return (optionalBuddyMatched.orElseThrow(() -> new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND)));
	}

	public BuddyMatched getBuddyMatchedByBuddy(Buddy buddy) {

		return buddyMatchedRepositoryImpl.findByOwnerOrPartner(buddy)
			.orElseThrow(() -> new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND));

	}

	public Buddy getOtherBuddyInBuddyMatched(BuddyMatched buddyMatched, Buddy ownerBuddy) {
		if (buddyMatched.getOwner() == ownerBuddy) {
			return buddyMatched.getPartner();
		} else {
			return buddyMatched.getOwner();
		}
	}

	private void sendMatchingSuccessMessage(Buddy matchingSuccessBuddy) {
		String phoneNumber = matchingSuccessBuddy.getMember().getPhoneNumber();
		smsService.sendSms(phoneNumber, SmsText.MATCHING_COMPLETE_BUDDY);
	}

	public void sendMatchingFailurePenaltyMessage(Buddy matchingRejectBuddy, SmsText smsMatchingFailText) {
		String phoneNumber = matchingRejectBuddy.getMember().getPhoneNumber();
		smsService.sendSms(phoneNumber, smsMatchingFailText);
	}
}
