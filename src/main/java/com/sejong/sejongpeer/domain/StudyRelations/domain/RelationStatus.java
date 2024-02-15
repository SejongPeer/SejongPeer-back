package com.sejong.sejongpeer.domain.StudyRelations.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RelationStatus {
	PENDING("대기"),
	ACCEPT("수락"),
	REJECT("거부");

	private final String value;
}
