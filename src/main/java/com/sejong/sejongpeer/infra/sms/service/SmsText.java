package com.sejong.sejongpeer.infra.sms.service;

public enum SmsText {
	MATCHING_FOUND_BUDDY("버디가 매칭되었습니다. 접속하여 확인해주세요!"),
	MATCHING_FOUND_HONBAB("혼밥이 매칭되었습니다. 접속하여 확인해주세요!");

	private final String value;

	SmsText(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
