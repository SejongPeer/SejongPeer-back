package com.sejong.sejongpeer.domain.scrap.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScrapType {

	STUDY("스터디"),
	;
	private String value;
}
