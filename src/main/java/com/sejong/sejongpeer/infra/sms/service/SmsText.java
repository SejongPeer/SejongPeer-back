package com.sejong.sejongpeer.infra.sms.service;

public enum SmsText {
	MATCHING_FOUND_BUDDY("[세종피어] 버디를 찾았습니다! \n"
		+ "세종버디에 재접속해 버디정보(학과, 학년) 확인 후 수락여부를 결정해주세요.");

	private final String value;

	SmsText(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
