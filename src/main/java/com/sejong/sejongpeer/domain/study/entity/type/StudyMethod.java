package com.sejong.sejongpeer.domain.study.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyMethod {

	FACE_TO_FACE("대면"),
	NON_FACE_TO_FACE("비대면"),
	BOTH("대면 & 비대면");


	private final String value;
}
