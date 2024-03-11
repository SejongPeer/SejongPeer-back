package com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum HonbabMatchedStatus {
	MATCHING_COMPLETED("매칭 성사");

	private final String value;
}
