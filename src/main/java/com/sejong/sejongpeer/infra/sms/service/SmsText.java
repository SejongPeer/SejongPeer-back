package com.sejong.sejongpeer.infra.sms.service;

public enum SmsText {
	MATCHING_FOUND_HONBAB("[세종피어] 밥짝꿍 매칭에 성공했습니다!\n"
		+ "혼밥탈출에 다시 접속해 밥짝꿍의 정보를 확인해주세요(15분 내)"),
	MATCHING_FOUND_BUDDY("[세종피어] 버디를 찾았습니다! \n"
		+ "세종버디에 재접속해 버디정보(학과, 학년) 확인 후 수락여부를 결정해주세요."),
	MATCHING_COMPLETE_BUDDY("[세종피어]버디 매칭에 성공했습니다!\n" +
		"세종버디에 다시 접속해 버디의 정보를 모두 확인해주세요 :)"),
	MATCHING_FAILED("[세종피어]매칭에 실패했습니다!\n" +
		"(거절한 버디에겐 1시간 이용제한 페널티가 부여됩니다)");

	private final String value;

	SmsText(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
