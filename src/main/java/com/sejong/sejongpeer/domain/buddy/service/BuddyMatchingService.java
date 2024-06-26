package com.sejong.sejongpeer.domain.buddy.service;

import com.sejong.sejongpeer.domain.buddy.dto.request.MatchingResultRequest;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepository;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.SecurityUtil;
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
	private final BuddyRepository buddyRepository;
	private final SmsService smsService;
	private final SecurityUtil securityUtil;

	public void updateBuddyMatchedStatus(MatchingResultRequest request) {
		final String memberId = securityUtil.getCurrentMemberId();
		Buddy ownerLatestBuddy = buddyRepository.findTopByMemberIdAndStatusOrderByCreatedAtDesc(memberId,
				BuddyStatus.FOUND_BUDDY)
			.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		changeOwnerLatestBuddyStatusByIsAccept(ownerLatestBuddy, request.isAccept());

		BuddyMatched progressMatch = getBuddyMatchedByBuddy(ownerLatestBuddy);

		Buddy partnerBuddy = getOtherBuddyInBuddyMatched(progressMatch, ownerLatestBuddy);

		updateStatusBasedOnBuddies(progressMatch, ownerLatestBuddy, partnerBuddy);

		buddyMatchedRepository.save(progressMatch);
	}

	private void changeOwnerLatestBuddyStatusByIsAccept(Buddy ownerLatestBuddy, boolean accept) {
		if (!accept) {
			ownerLatestBuddy.changeStatus(BuddyStatus.REJECT);
		} else {
			ownerLatestBuddy.changeStatus(BuddyStatus.ACCEPT);
		}
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

		sendMatchingFailurePenaltyMessage(ownerBuddy.getMember().getPhoneNumber(), SmsText.MATCHING_FAILED);
		sendMatchingFailurePenaltyMessage(targetBuddy.getMember().getPhoneNumber(), SmsText.MATCHING_FAILED);
	}

	private void handleBuddyMatchedSuccess(BuddyMatched buddyMatched, Buddy ownerBuddy, Buddy targetBuddy) {
		buddyMatched.changeStatus(BuddyMatchedStatus.MATCHING_COMPLETED);
		ownerBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);
		targetBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);

		sendMatchingSuccessMessage(ownerBuddy.getMember().getPhoneNumber());
		sendMatchingSuccessMessage(targetBuddy.getMember().getPhoneNumber());
	}

	public BuddyMatched getLatestBuddyMatched(Buddy buddy) {
		Optional<BuddyMatched> optionalBuddyMatched = buddyMatchedRepository.findLatestByOwnerOrPartner(buddy);
		return (optionalBuddyMatched.orElseThrow(() -> new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND)));
	}

	public BuddyMatched getBuddyMatchedByBuddy(Buddy buddy) {
		if (buddy.getMatchedAsOwner() != null) {
			return buddy.getMatchedAsOwner();
		} else {
			return buddy.getMatchedAsPartner();
		}
	}

	public Buddy getOtherBuddyInBuddyMatched(BuddyMatched buddyMatched, Buddy ownerBuddy) {
		if (buddyMatched.getOwner() == ownerBuddy) {
			return buddyMatched.getPartner();
		} else {
			return buddyMatched.getOwner();
		}
	}

	private void sendMatchingSuccessMessage(String phoneNumber) {
		smsService.sendSms(phoneNumber, SmsText.MATCHING_COMPLETE_BUDDY);
	}

	public void sendMatchingFailurePenaltyMessage(String phoneNumber, SmsText smsMatchingFailText) {
		smsService.sendSms(phoneNumber, smsMatchingFailText);
	}
}
