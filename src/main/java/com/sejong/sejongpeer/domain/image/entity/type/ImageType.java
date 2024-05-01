package com.sejong.sejongpeer.domain.image.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
	STUDY("study"),
	;
	private final String value;
}
