package com.sejong.sejongpeer.domain.study.dto.response;

import java.util.List;

public record StudyTotalPostResponse(
	String title,
	String createdAt,
	Boolean isImage,
	List<String> postTag

) {
}
