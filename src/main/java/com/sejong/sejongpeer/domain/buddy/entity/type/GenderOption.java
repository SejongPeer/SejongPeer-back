package com.sejong.sejongpeer.domain.buddy.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderOption {
	SAME("동일한 성별"),
	NONE("다른 성별");
	private final String value;
}
