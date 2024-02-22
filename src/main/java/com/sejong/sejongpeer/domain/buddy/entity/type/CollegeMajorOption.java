package com.sejong.sejongpeer.domain.buddy.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CollegeMajorOption {
	SAME_COLLEGE("같은 단과대"),
	SAME_DEPARTMENT("같은 학과"),
	NO_MATTER("상관없음");
	private final String value;
}
