package com.sejong.sejongpeer.domain.study.dto.response;

import java.time.LocalDateTime;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;

public record StudyFindResponse(
	Long studyId,
	String title,
	String content,
	Integer recruitmentCount,
	StudyType type,
	RecruitmentStatus recruitmentStatus,
	LocalDateTime recruitmentStartAt,
	LocalDateTime recruitmentEndAt
) {
	public static StudyFindResponse from(Study study) {
		return new StudyFindResponse(
			study.getId(),
			study.getTitle(),
			study.getContent(),
			study.getRecruitmentCount(),
			study.getType(),
			study.getRecruitmentStatus(),
			study.getRecruitmentStartAt(),
			study.getRecruitmentEndAt()
		);
	}
}
