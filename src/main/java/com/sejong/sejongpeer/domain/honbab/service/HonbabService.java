package com.sejong.sejongpeer.domain.honbab.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.honbab.dto.request.RegisterHonbabRequest;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HonbabService {
	private final HonbabRepository honbabRepository;
	private final MemberRepository memberRepository;

	public void registerHonbab(RegisterHonbabRequest request, String memberId) {
		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		checkPossibleRegistration(memberId);

		Honbab honbab = createHonbabEntity(request, member);
		honbabRepository.save(honbab);
	}

	private Honbab createHonbabEntity(RegisterHonbabRequest createHonbabRequest, Member member) {
		return Honbab.createHonbab(
			member,
			HonbabStatus.IN_PROGRESS,
			createHonbabRequest.genderOption(),
			createHonbabRequest.menuCategoryOption()
		);
	}
	private void checkPossibleRegistration(String memberId) {
		Optional<Honbab> optionalHonbab = getLastHonbabByMemberId(memberId);

		optionalHonbab.ifPresent(latestHonbab -> {
			if (latestHonbab.getStatus() == HonbabStatus.IN_PROGRESS) {
				throw new CustomException(ErrorCode.REGISTRATION_NOT_POSSIBLE);
			}
		});
	}

	private Optional<Honbab> getLastHonbabByMemberId(String memberId) {
		return honbabRepository.findLastHonbabByMemberId(memberId);
	}
}
