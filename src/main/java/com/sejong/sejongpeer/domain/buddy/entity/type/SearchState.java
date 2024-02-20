package com.sejong.sejongpeer.domain.buddy.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchState {
	IN_PROGRESS("찾는 중"),
	FOUND_BUDDY("찾음");
	private final String value;
}
