package com.sejong.sejongpeer.domain.buddy.service;

import com.sejong.sejongpeer.domain.buddy.dto.request.MatchingResultRequest;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepository;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
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
	private final BuddyRepository buddyRepository;
	private final MemberRepository memberRepository;
	private final SmsService smsService;

	public void updateBuddyMatchingStatus(String memberId, MatchingResultRequest request) {
		Member owner = getMemberById(memberId);

		Optional<Buddy> optionalOwnerLatestBuddy = buddyRepository.findTopByMemberAndStatusOrderByCreatedAtDesc(owner, BuddyStatus.IN_PROGRESS);
		Buddy ownerLatestBuddy = optionalOwnerLatestBuddy.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		if (request.isAccept()) {
			ownerLatestBuddy.changeStatus(BuddyStatus.ACCEPT);
		} else {
			ownerLatestBuddy.changeStatus(BuddyStatus.REJECT);
		}

		Buddy targetBuddy = findTargetBuddy(ownerLatestBuddy);

		BuddyMatched existingMatch = buddyMatchedRepository.findByOwnerAndPartner(ownerLatestBuddy, targetBuddy).orElseThrow(() -> new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND));

		updateStatusBasedOnBuddies(existingMatch, ownerLatestBuddy, targetBuddy);

		buddyMatchedRepository.save(existingMatch);
	}

	private Member getMemberById(String memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private void updateStatusBasedOnBuddies(BuddyMatched buddyMatched, Buddy ownerBuddy, Buddy targetBuddy) {

		// FOUND_BUDDY 일 경우, ownerBuddy는 ACCEPT, REJECT 선택만 가능
		if (ownerBuddy.getStatus() != BuddyStatus.ACCEPT || targetBuddy.getStatus() != BuddyStatus.ACCEPT) {
			// 매칭에 필요한 조건을 만족하지 않으면 MATCHING_FAIL 처리
			buddyMatched.changeStatus(BuddyMatchedStatus.MATCHING_FAIL);
			ownerBuddy.changeStatus(BuddyStatus.REJECT);
			targetBuddy.changeStatus(BuddyStatus.DENIED);
			return;
		}
	
		handlerBuddyMatchedSuccess(buddyMatched, ownerBuddy, targetBuddy);
	}

	private void handlerBuddyMatchedSuccess(BuddyMatched buddyMatched, Buddy ownerBuddy, Buddy targetBuddy) {
		// 매칭이 성공할 경우 처리
		buddyMatched.changeStatus(BuddyMatchedStatus.MATCHING_COMPLETED);
		ownerBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);
		targetBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);

		// ownerBuddy와 targetBuddy에게 버디 매칭 성공 문자 발송
		sendMatchingMessage(ownerBuddy);
		sendMatchingMessage(targetBuddy);
	}

	private Buddy findTargetBuddy(Buddy ownerBuddy) {
		BuddyMatched selectedBuddyMatched = getLatestBuddyMatched(ownerBuddy);

		return getOtherBuddyInBuddyMatched(selectedBuddyMatched, ownerBuddy);
	}

	public BuddyMatched getLatestBuddyMatched(Buddy buddy) {
		Optional<BuddyMatched> optionalBuddyMatched = buddyMatchedRepository.findLatestByOwnerOrPartner(buddy);
		return  (optionalBuddyMatched.orElseThrow(() -> new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND)));
	}

	public   Buddy getOtherBuddyInBuddyMatched(BuddyMatched buddyMatched, Buddy ownerBuddy) {
		if (buddyMatched.getOwner() == ownerBuddy) {
			return buddyMatched.getPartner();
		} else {
			return buddyMatched.getOwner();
		}
	}

	private void sendMatchingMessage(Buddy matchingSuccessBuddy) {
		String phoneNumber = matchingSuccessBuddy.getMember().getPhoneNumber();
		smsService.sendSms(phoneNumber, SmsText.MATCHING_COMPLETE_BUDDY);
	}
}
