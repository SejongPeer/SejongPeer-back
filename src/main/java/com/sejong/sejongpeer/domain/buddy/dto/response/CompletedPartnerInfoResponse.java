package com.sejong.sejongpeer.domain.buddy.dto.response;

public record CompletedPartnerInfoResponse(
	String collegeMajor,
	Integer grade,
	String name,
	String kakaoAccount
) {
}