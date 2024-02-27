package com.sejong.sejongpeer.infra.sms.service;

public enum SmsText {
	MATCHING_FOUND_BUDDY("버디가 매칭되었습니다. 접속하여 확인해주세요!"),
	MATCHING_COMPLETE_BUDDY("[세종피어] 버디 매칭에 성공했습니다! 혼밥탈출에 다시 접속해 밥짝꿍의 정보를 확인해주세요:)");

	private final String value;

	SmsText(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
