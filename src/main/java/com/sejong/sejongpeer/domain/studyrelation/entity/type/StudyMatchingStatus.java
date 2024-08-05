package com.sejong.sejongpeer.domain.studyrelation.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyMatchingStatus {
	PENDING("대기"),
	ACCEPT("수락"),
	REJECT("거절"),
	CANCEL("취소");

	private final String value;
}
