package com.sejong.sejongpeer.domain.member.dto.response;

public record ExistsCheckResponse(Boolean isExist) {
	public static ExistsCheckResponse of(Boolean isExist) {
		return new ExistsCheckResponse(isExist);
	}
}
