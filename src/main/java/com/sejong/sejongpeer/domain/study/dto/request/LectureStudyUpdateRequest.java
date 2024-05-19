package com.sejong.sejongpeer.domain.study.dto.request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record LectureStudyUpdateRequest(
	@Size(max = 50, message = "제목은 50자 이하 입니다.")
	@Schema(description = "스터디 제목")
	String title,
	@Schema(description = "모집 인원") Integer recruitmentCount,
	@Schema(description = "스터디 내용") String content,
	@Schema(description = "강의 ID") Long lectureId,
	@Schema(description = "모집 시작 기간") LocalDateTime recruitmentStartAt,
	@Schema(description = "모집 마감  기간") LocalDateTime recruitmentEndAt) {
}
