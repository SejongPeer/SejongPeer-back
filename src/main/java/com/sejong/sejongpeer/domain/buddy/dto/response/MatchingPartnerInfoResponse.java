package com.sejong.sejongpeer.domain.buddy.dto.response;

import com.sejong.sejongpeer.domain.member.entity.Member;

public record MatchingPartnerInfoResponse(
	String collegeMajor,
	Integer grade
) {
	public static MatchingPartnerInfoResponse memberFrom(Member member) {
		return new MatchingPartnerInfoResponse(
			member.getCollegeMajor().getMajor(),
			member.getGrade()
		);
	}
}
