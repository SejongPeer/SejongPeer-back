package com.sejong.sejongpeer.domain.scrap.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyScrapCreateRequest(
	@Schema(description = "스터디 ID", example = "1")
	Long studyId
) {
}
