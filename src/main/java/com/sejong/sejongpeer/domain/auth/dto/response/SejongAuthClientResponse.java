package com.sejong.sejongpeer.domain.auth.dto.response;

import com.sejong.sejongpeer.domain.member.entity.Member;

public record SejongAuthClientResponse(
	String msg,
	String grade,
	String major,
	String name,
	String status,
	Boolean isAuth,
	Integer statusCode,
	Boolean success
) {
	public static SejongAuthClientResponse of(String msg, String grade, String major, String name, String status,
		Boolean isAuth, Integer statusCode, Boolean success) {
		return new SejongAuthClientResponse(
			msg, grade, major, name, status, isAuth, statusCode, success);
	}
}
