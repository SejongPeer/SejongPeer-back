package com.sejong.sejongpeer.domain.study.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StudyUpdateRequest(
	@NotBlank(message = "스터디 제목은 비워둘 수 없습니다.")
	@Size(max = 50, message = "제목은 50자 이하 입니다.")
	@Schema(description = "스터디 제목") String title,
	@Schema(description = "모집 인원") Integer recruitmentCount,
	@NotBlank(message = "스터디 내용은 비워둘 수 없습니다.")
	@Schema(description = "스터디 내용") String content,
	@NotNull(message = "모집 마감 시간은 비워둘 수 없습니다.")
	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "yyyy-MM-dd HH:mm:ss",
		timezone = "Asia/Seoul")
	@Schema(
		description = "모집 마감 시간",
		defaultValue = "2023-01-03 00:00:00",
		type = "string") LocalDateTime recruitmentEndAt) {
}
