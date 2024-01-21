package com.sejong.sejongpeer.security.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class HeaderConstants {
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String ACCESS_TOKEN_HEADER = "Authorization";
	public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
}
