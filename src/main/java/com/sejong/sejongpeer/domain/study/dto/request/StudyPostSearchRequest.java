package com.sejong.sejongpeer.domain.study.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record StudyPostSearchRequest(

	@NotNull(message = "검색 시 모집 인원 하한선은 비워둘 수 없습니다.")
	@Min(value = 2, message = "모집 최소 인원은 2명 이상이어야 합니다.")
	@Schema(description = "모집 최소 인원") Integer recruitmentMin,

	@NotNull(message = "검색 시 모집 인원 상한선은 비워둘 수 없습니다.")
	@Max(value = 9, message = "모집 최대 인원은 9명 이하이어야 합니다.")
	@Schema(description = "모집 최대 인원") Integer recruitmentMax,

	@NotNull(message = "모집 시작 날짜는 비워둘 수 없습니다.")
	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "yyyy-MM-dd HH:mm:ss",
		timezone = "Asia/Seoul")
	@Schema(
		description = "모집 시작 시간",
		defaultValue = "2024-05-18 00:00:00",
		type = "string") LocalDateTime recruitmentStartAt,
	@NotNull(message = "모집 마감 날짜는 비워둘 수 없습니다.")
	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "yyyy-MM-dd HH:mm:ss",
		timezone = "Asia/Seoul")
	@Schema(
		description = "모집 마감 시간",
		defaultValue = "2023-05-19 00:00:00",
		type = "string") LocalDateTime recruitmentEndAt,

	@NotNull(message = "스터디 모집 여부는 비워둘 수 없습니다.")
	@Schema(description = "스터디 모집 여부에 대해서 모집 중은 true, 모집 마감은 false로 요청주세요.")
	Boolean isRecruiting,

	@NotBlank(message = "검색어는 비워둘 수 없습니다.")
	@Schema(description = "검색어") String searchWord

) {
}
