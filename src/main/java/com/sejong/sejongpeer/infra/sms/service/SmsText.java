package com.sejong.sejongpeer.infra.sms.service;

public enum SmsText {
	MATCHING_FOUND_HONBAB("[세종피어] 대동지 매칭에 성공했습니다!\n"
		+ "혼축탈출에 다시 접속해 대동지의 정보를 확인해주세요.(당일 내)"),
	MATCHING_FOUND_BUDDY("[세종피어] 버디를 찾았습니다! \n"
		+ "세종버디에 재접속해 버디정보(학과, 학년) 확인 후 수락여부를 결정해주세요."),
	MATCHING_COMPLETE_BUDDY("[세종피어]버디 매칭에 성공했습니다!\n" +
		"세종버디에 다시 접속해 버디의 정보를 모두 확인해주세요 :)"),
	MATCHING_FAILED("[세종피어]매칭에 실패했습니다! 다시 신청해주세요.\n" +
		"(거절한 버디에겐 1시간 이용제한 페널티가 부여됩니다)"),

	MATCHING_AUTO_FAILED_DENIED("[세종피어]상대방이 24시간 내에 매칭 확정을 하지 않아 자동 거절처리되었습니다.\n" +
		"다시 신청해주세요!"),
	MATCHING_AUTO_FAILED_REJECT("[세종피어]24시간 내에 매칭확정을 하지 않아 거절처리 되었습니다. (1시간 제한 패널티)");

	private final String value;

	SmsText(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
