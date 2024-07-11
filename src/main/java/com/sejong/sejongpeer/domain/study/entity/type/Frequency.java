package com.sejong.sejongpeer.domain.study.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Frequency {

	ONCE_OR_TWICE_A_WEEK("주 1~2회"),
	THREE_TO_FOUR_TIMES_A_WEEK("주 3~4회"),
	FIVE_OR_MORE_TIMES_A_WEEK("주 5회 이상");

	private final String value;
}
