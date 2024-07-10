package com.sejong.sejongpeer.domain.study.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Frequency {

	WEEKLY_1_2("주 1~2회"),
	WEEKLY_3_4("주 3~4회"),
	WEEKLY_5_MORE("주 5회 이상");

	private final String value;
}
