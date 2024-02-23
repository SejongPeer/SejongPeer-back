package com.sejong.sejongpeer.domain.buddy.service;

import static com.sejong.sejongpeer.domain.buddy.entity.buddy.type.Status.*;

import java.util.Optional;

import com.sejong.sejongpeer.domain.buddy.dto.request.RegisterRequest;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.Status;
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
			Status.IN_PROGRESS,
			createBuddyRequest.isSubMajor()
		);
	}

	public void cancelBuddy(String memberId) {
		Buddy buddy = getLastBuddyByMemberId(memberId);

		if (buddy.getStatus() == IN_PROGRESS) {
			buddy.changeStatus(CANCEL);
			buddyRepository.save(buddy);
		} else {
			throw new CustomException(ErrorCode.NOT_IN_PROGRESS);
		}
	}

	private Buddy getLastBuddyByMemberId(String memberId) {
		Optional<Buddy> buddyOptional = buddyRepository.findLastBuddyByMemberId(memberId);

		return buddyOptional.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));
	}


}
