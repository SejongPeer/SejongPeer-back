package com.sejong.sejongpeer.domain.study.dto.response;

public record StudyPostInfoResponse(
	String title,
	String writerMajor,
	String writerNickname,
	String recruitmentStart,
	String recruitmentEnd,
	String content,
	String categoryName
) {
}
