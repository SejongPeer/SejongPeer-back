package com.sejong.sejongpeer.domain.buddy.service;

import static com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.sejong.sejongpeer.domain.buddy.dto.request.RegisterRequest;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingStatusResponse;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class BuddyService {
	private final BuddyRepository buddyRepository;
	private final MemberRepository memberRepository;
	public void registerBuddy(RegisterRequest request, String memberId) {
		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Buddy latestBuddy = buddyRepository.findTopByMemberOrderByUpdatedAtDesc(member)
		.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		if (latestBuddy != null && latestBuddy.getStatus() == BuddyStatus.REJECT &&
			latestBuddy.getUpdatedAt().plusHours(1).isAfter(LocalDateTime.now())) {
			throw new CustomException(ErrorCode.REJECT_PENALTY);
		}
		
		checkPossibleRegistration(memberId);

		
		Buddy buddy = createBuddyEntity(request, member);
		buddyRepository.save(buddy);
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
			if (latestBuddy.getStatus() == BuddyStatus.IN_PROGRESS ||
				(latestBuddy.getStatus().equals(BuddyStatus.REJECT) &&
					!isPossibleFromUpdateAt(latestBuddy.getUpdatedAt()))) {
				throw new CustomException(ErrorCode.REGISTRATION_NOT_POSSIBLE);
			}
		});
	}

	private Optional<Buddy> getLastBuddyByMemberId(String memberId) {
		return buddyRepository.findLastBuddyByMemberId(memberId);
	}

	private boolean isPossibleFromUpdateAt(LocalDateTime updatedAt) {
		LocalDateTime oneHourAfterTime = updatedAt.plusHours(1);

		return LocalDateTime.now().isAfter(oneHourAfterTime);
	}

	@Transactional(readOnly = true)
	public MatchingStatusResponse getMatchingStatus(String memberId) {
		Optional<Buddy> optionalBuddy = getLastBuddyByMemberId(memberId);
		Buddy buddy = optionalBuddy.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		return new MatchingStatusResponse(buddy.getStatus());
	}
}

