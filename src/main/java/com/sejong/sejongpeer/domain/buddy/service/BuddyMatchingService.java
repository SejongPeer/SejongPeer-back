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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BuddyMatchingService {

	private final BuddyMatchedRepository buddyMatchedRepository;
	private final BuddyRepository buddyRepository;
	private final MemberRepository memberRepository;

	public void updateBuddyMatchingStatus(String memberId, MatchingResultRequest request) {
		Member owner = getMemberById(memberId);

		List<Buddy> ownerBuddies = buddyRepository.findAllByMemberOrderByCreatedAtDesc(owner);
		Buddy ownerLatestBuddy = ownerBuddies.isEmpty() ? null : ownerBuddies.get(0);
		ownerLatestBuddy.changeStatus(request.buddyStatus());

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
			buddyMatched.setStatus(BuddyMatchedStatus.MATCHING_FAIL);
			ownerBuddy.changeStatus(BuddyStatus.REJECT);
			targetBuddy.changeStatus(BuddyStatus.DENIED);
			return;
		}
	
		// 매칭이 성공할 경우 처리
		buddyMatched.setStatus(BuddyMatchedStatus.MATCHING_COMPLETED);
		ownerBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);
		targetBuddy.changeStatus(BuddyStatus.MATCHING_COMPLETED);
	}

	private Buddy findTargetBuddy(Buddy ownerBuddy) {
		Optional<BuddyMatched> optionalBuddyMatched = buddyMatchedRepository.findLatestByOwnerOrPartner(ownerBuddy);

		return optionalBuddyMatched.map(BuddyMatched::getPartner).orElseThrow(() -> new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND));
	}
}
