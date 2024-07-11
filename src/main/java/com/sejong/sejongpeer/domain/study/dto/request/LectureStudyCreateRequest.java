package com.sejong.sejongpeer.domain.study.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sejong.sejongpeer.domain.study.entity.type.Frequency;
import com.sejong.sejongpeer.domain.study.entity.type.StudyMethod;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LectureStudyCreateRequest(
	@NotBlank(message = "스터디 제목은 비워둘 수 없습니다.")
	@Size(max = 50, message = "제목은 50자 이하 입니다.")
	@Schema(description = "스터디 제목") String title,
	@NotBlank(message = "스터디 내용은 비워둘 수 없습니다.")
	@Schema(description = "스터디 내용") String content,
	@Schema(description = "모집 인원") Integer recruitmentCount,

	@NotNull(message = "스터디 방식은 비워둘 수 없습니다.")
	@Schema(description = "스터디 방식") StudyMethod method,

	@NotNull(message = "모집빈도는 비워둘 수 없습니다.")
	@Schema(description = "모집빈도") Frequency frequency,

	@NotBlank(message = "카카오톡 오픈채팅 링크는 비워둘 수 없습니다.")
	@Schema(description = "카카오톡 채팅 링크") String kakaoLink,
	@Schema(description = "질문 링크") String questionLink,

	@NotNull(message = "강의 ID는 비워둘 수 없습니다.")
	@Schema(description = "강의 ID") Long lectureId,

	@Schema(description = "태그 리스트") List<String> tags,

	@NotNull(message = "모집 시작 시간은 비워둘 수 없습니다.")
	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "yyyy-MM-dd HH:mm:ss",
		timezone = "Asia/Seoul")
	@Schema(
		description = "모집 시작 시간",
		defaultValue = "2023-01-03 00:00:00",
		type = "string") LocalDateTime recruitmentStartAt,
	@NotNull(message = "모집 마감 시간은 비워둘 수 없습니다.")
	@JsonFormat(
		shape = JsonFormat.Shape.STRING,
		pattern = "yyyy-MM-dd HH:mm:ss",
		timezone = "Asia/Seoul")
	@Schema(
		description = "모집 마감 시간",
		defaultValue = "2023-01-03 00:00:00",
		type = "string") LocalDateTime recruitmentEndAt

) {
}
