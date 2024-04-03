package com.sejong.sejongpeer.domain.buddy.dto.response;

public record ActiveCustomersCountResponse(Long count) {
	public static ActiveCustomersCountResponse of(Long count) {
		return new ActiveCustomersCountResponse(count);
	}
}
