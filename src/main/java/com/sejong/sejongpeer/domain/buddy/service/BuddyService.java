package com.sejong.sejongpeer.domain.buddy.service;

import com.sejong.sejongpeer.domain.buddy.dto.request.RegisterRequest;
import com.sejong.sejongpeer.domain.buddy.entity.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.type.MatchingStatus;
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
	public void register(RegisterRequest request, String memberId) {
		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

		Buddy buddy = createBuddyEntity(request, member);
		buddyRepository.save(buddy);
	}

	private Buddy createBuddyEntity(RegisterRequest createBuddyRequest, Member member) {
		return Buddy.createBuddy(
			member,
			createBuddyRequest.genderOption(),
			createBuddyRequest.buddyType(),
			createBuddyRequest.buddyRange(),
			createBuddyRequest.gradeOption(),
			MatchingStatus.IN_PROGRESS,
			createBuddyRequest.isSubMajor()
		);
	}
}
