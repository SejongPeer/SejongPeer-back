package com.sejong.sejongpeer.domain.buddy.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GradeOption {
	GRADE_1("1학년"),
	GRADE_2("2학년"),
	GRADE_3("3학년"),
	GRADE_4("4학년"),
	NO_MATTER("상관없음");
	private final String value;
}
