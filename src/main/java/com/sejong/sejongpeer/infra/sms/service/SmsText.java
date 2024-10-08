package com.sejong.sejongpeer.infra.sms.service;

public enum SmsText {
	MATCHING_FOUND_HONBAB("[세종피어] 밥짝꿍 매칭에 성공했습니다!\n"
		+ "혼밥탈출에 다시 접속해 밥짝꿍의 정보를 확인해주세요(15분 내)"),
	MATCHING_FOUND_BUDDY("[세종피어] 버디를 찾았습니다! \n"
		+ "세종버디에 재접속해 버디정보(학과, 학년) 확인 후 수락여부를 결정해주세요."),
	MATCHING_COMPLETE_BUDDY("[세종피어]버디 매칭에 성공했습니다!\n" +
		"세종버디에 다시 접속해 버디의 정보를 모두 확인해주세요 :)"),
	MATCHING_FAILED("[세종피어]매칭에 실패했습니다! 다시 신청해주세요.\n" +
		"(거절한 버디에겐 1시간 이용제한 페널티가 부여됩니다)"),

	MATCHING_AUTO_FAILED_DENIED("[세종피어]상대방이 24시간 내에 매칭 확정을 하지 않아 자동 거절처리되었습니다.\n" +
		"다시 신청해주세요!"),
	MATCHING_AUTO_FAILED_REJECT("[세종피어]24시간 내에 매칭확정을 하지 않아 거절처리 되었습니다. (1시간 제한 패널티)"),
	STUDY_RECRUITMENT_COMPLETED("스터디 모집 완료!\n"),
	STUDY_APPLY_ALARM("[세종피어] 스터디 지원 알림, 수락여부를 결정하세요! sejongpeer.co.kr/mypost"),
	STUDY_POST_DELETION_ALARM("신청하신 스터디 게시글이 삭제되었습니다."),
	STUDY_APPLY_REJECT_ALARM("스터디 매칭에 실패했습니다.");

	private final String value;

	SmsText(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
