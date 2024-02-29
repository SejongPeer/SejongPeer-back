package com.sejong.sejongpeer.domain.honbab.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.honbab.dto.request.RegisterHonbabRequest;
import com.sejong.sejongpeer.domain.honbab.dto.response.HonbabMatchingStatusResponse;
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

		Honbab honbab = Honbab.createHonbab(member, request);
		honbabRepository.save(honbab);
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

	@Transactional(readOnly = true)
	public HonbabMatchingStatusResponse getHonbabMatchingStatus(String memberId) {
		Optional<Honbab> optionalHonbab = getLastHonbabByMemberId(memberId);
		Honbab honbab = optionalHonbab.orElseThrow(() -> new CustomException(ErrorCode.BUDDY_NOT_FOUND));

		return HonbabMatchingStatusResponse.honbabFrom(honbab);
	}
}
