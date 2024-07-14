package com.sejong.sejongpeer.domain.studyrelation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.Tag;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;

public record AppliedStudyResponse(
	Long studyId,
	String title,
	Integer recruitmentCount,
	Integer participantsCount,
	StudyType type,

	List<Tag> tags,
	RecruitmentStatus recruitmentStatus,
	LocalDateTime recruitmentStartAt,
	LocalDateTime recruitmentEndAt) {
	public static AppliedStudyResponse of(Study study, List<Tag> tags) {
		return new AppliedStudyResponse(
			study.getId(),
			study.getTitle(),
			study.getRecruitmentCount(),
			study.getParticipantsCount(),
			study.getType(),
			tags,
			study.getRecruitmentStatus(),
			study.getRecruitmentStartAt(),
			study.getRecruitmentEndAt());
	}
}


