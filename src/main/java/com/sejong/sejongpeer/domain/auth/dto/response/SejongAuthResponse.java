package com.sejong.sejongpeer.domain.auth.dto.response;

import java.util.Map;

public record SejongAuthResponse(
	String msg,
	Result result,
	String version
) {
	public record Result(
		String authenticator,
		Body body,
		String code,
		boolean is_auth,
		int status_code,
		boolean success
	) {
		public record Body(
			String grade,
			String major,
			String name,
			Map<String, String> read_certification,
			String status
		) {
		}
	}
}
