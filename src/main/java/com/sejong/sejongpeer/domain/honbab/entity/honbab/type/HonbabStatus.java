package com.sejong.sejongpeer.domain.honbab.entity.honbab.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum HonbabStatus {
	IN_PROGRESS("매칭 중"),
	CANCEL("중도 취소"),
	MATCHING_COMPLETED("매칭 성사"),
	TIME_OUT("시간 초과"),
	EXPIRED("재신청 가능");

	private final String value;
}
