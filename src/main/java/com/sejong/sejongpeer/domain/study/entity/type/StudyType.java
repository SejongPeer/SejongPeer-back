package com.sejong.sejongpeer.domain.study.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyType {
	LECTURE("학교 수업"),
	EXTERNAL_ACTIVITY("수업 외 활동");

	private final String value;
}
