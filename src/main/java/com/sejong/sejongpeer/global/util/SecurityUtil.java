package com.sejong.sejongpeer.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

@Component
public class SecurityUtil {

	public String getCurrentMemberId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try {
			return authentication.getName();
		} catch (Exception e) {
			throw new CustomException(ErrorCode.AUTH_NOT_FOUND);
		}
	}
}
