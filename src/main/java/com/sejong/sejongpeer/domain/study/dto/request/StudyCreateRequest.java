package com.sejong.sejongpeer.domain.study.dto.request;

import java.time.LocalDateTime;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudyCreateRequest(
	@NotBlank(message = "스터디 제목은 비워둘 수 없습니다.")
	@Size(max = 50, message = "제목은 50자 이하 입니다.")
		@Schema(description = "스터디 제목")
	String title,
	@NotBlank(message = "스터디 내용은 비워둘 수 없습니다.")
	@Schema(description = "스터디 내용")
	String content,
	@Schema(description = "모집 인원")
	Integer recruitmentCount,
	@Schema(description = "스터디 유형 (ex. 공모전, 대외활동 등등")
	StudyType type,
	@Schema(description = "모집 시작 기간")
	LocalDateTime recruitmentStartAt,
	@Schema(description = "모집 마감  기간")
	LocalDateTime recruitmentEndAt
) {
}
