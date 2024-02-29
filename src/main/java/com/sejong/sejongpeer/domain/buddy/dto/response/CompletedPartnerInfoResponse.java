package com.sejong.sejongpeer.domain.buddy.dto.response;

import com.sejong.sejongpeer.domain.member.entity.Member;

public record CompletedPartnerInfoResponse(
	String collegeMajor,
	Integer grade,
	String name,
	String kakaoAccount
) {
	public static CompletedPartnerInfoResponse memberFrom(Member member) {
		return new CompletedPartnerInfoResponse(
			member.getCollegeMajor().getMajor(),
			member.getGrade(),
			member.getName(),
			member.getKakaoAccount()
		);
	}
}