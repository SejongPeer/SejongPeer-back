package com.sejong.sejongpeer.security.util;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContextUtil {
	public static String extractMemberId() {
		return (String)SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal();
	}
}
