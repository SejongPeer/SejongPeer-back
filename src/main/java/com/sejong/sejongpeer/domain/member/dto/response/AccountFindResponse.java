package com.sejong.sejongpeer.domain.member.dto.response;

import com.sejong.sejongpeer.domain.member.entity.Member;

public record AccountFindResponse(String account) {
	public static AccountFindResponse of(Member member) {
		return new AccountFindResponse(member.getAccount());
	}
}
