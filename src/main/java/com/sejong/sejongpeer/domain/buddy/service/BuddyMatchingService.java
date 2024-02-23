package com.sejong.sejongpeer.domain.buddy.service;

import com.sejong.sejongpeer.domain.buddy.dto.request.BuddyMatchingStatusUpdateRequest;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BuddyMatchingService {

	private final BuddyMatchedRepository buddyMatchedRepository;
	private final BuddyRepository buddyRepository;
	private final MemberRepository memberRepository;

	public void updateBuddyMatchingStatus(BuddyMatchingStatusUpdateRequest request) {
		Member owner = getMemberById(request.ownerBuddyId());
		Member target = getMemberById(request.partnerBuddyId());

		List<Buddy> ownerBuddies = buddyRepository.findAllByMemberOrderByCreatedAtDesc(owner);
		List<Buddy> targetBuddies = buddyRepository.findAllByMemberOrderByCreatedAtDesc(target);

		Buddy ownerLatestBuddy = ownerBuddies.isEmpty() ? null : ownerBuddies.get(0);
		Buddy targetLatestBuddy = targetBuddies.isEmpty() ? null : targetBuddies.get(0);

		BuddyMatched buddyMatched = BuddyMatched.registerMatchingPair(ownerLatestBuddy, targetLatestBuddy);

		updateStatusBasedOnBuddies(buddyMatched, ownerLatestBuddy, targetLatestBuddy);

		buddyMatchedRepository.save(buddyMatched);
	}

	private Member getMemberById(String memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	private void updateStatusBasedOnBuddies(BuddyMatched buddyMatched, Buddy ownerBuddy, Buddy targetBuddy) {
		if (ownerBuddy != null && targetBuddy != null) {
			BuddyMatchedStatus status;
			if (ownerBuddy.getBuddyStatus() == BuddyStatus.ACCEPT &&
				targetBuddy.getBuddyStatus() == BuddyStatus.ACCEPT) {
				status = BuddyMatchedStatus.MATCHING_COMPLETED;
			} else if (ownerBuddy.getBuddyStatus() == BuddyStatus.CANCEL ||
				targetBuddy.getBuddyStatus() == BuddyStatus.CANCEL ||
				ownerBuddy.getBuddyStatus() == BuddyStatus.REJECT ||
				targetBuddy.getBuddyStatus() == BuddyStatus.REJECT ||
				ownerBuddy.getBuddyStatus() == BuddyStatus.DENIED ||
				targetBuddy.getBuddyStatus() == BuddyStatus.DENIED) {
				status = BuddyMatchedStatus.MATCHING_FAIL;
			} else {
				status = BuddyMatchedStatus.IN_PROGRESS;
			}

			buddyMatched.setOwner(ownerBuddy);
			buddyMatched.setPartner(targetBuddy);
			buddyMatched.setBuddyMatchedStatus(status);
		} else {
			buddyMatched.setBuddyMatchedStatus(BuddyMatchedStatus.MATCHING_FAIL);
		}
	}


}
