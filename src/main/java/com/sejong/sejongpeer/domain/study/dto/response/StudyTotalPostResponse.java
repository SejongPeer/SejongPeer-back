package com.sejong.sejongpeer.domain.study.dto.response;

public record StudyTotalPostResponse(
	Long id,
	String title,
	String createdAt,
	Boolean isImage,
	String postTag

) {
}
