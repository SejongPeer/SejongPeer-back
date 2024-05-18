package com.sejong.sejongpeer.global.util;

import org.springframework.stereotype.Component;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberUtil {

	private final SecurityUtil securityUtil;
	private final MemberRepository memberRepository;

	public Member getCurrentMember() {
		return memberRepository
			.findById(securityUtil.getCurrentMemberId())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}

	public Member getMemberByMemberId(String memberId) {
		return memberRepository
			.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
	}
}
