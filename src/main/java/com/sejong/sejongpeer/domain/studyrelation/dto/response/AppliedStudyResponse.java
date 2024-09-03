package com.sejong.sejongpeer.domain.studyrelation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;

public record AppliedStudyResponse(
	Long studyId,
	String title,
	Integer recruitmentCount,
	Integer participantsCount,
	StudyType type,
	List<String> tags,
	Long scrapCount,
	RecruitmentStatus recruitmentStatus,

	boolean isScrappedStudy,
	LocalDateTime recruitmentStartAt,
	LocalDateTime recruitmentEndAt) {
	public static AppliedStudyResponse of(Study study, List<String> tags, Long scrapCount, 	boolean isScrappedStudy) {
		return new AppliedStudyResponse(
			study.getId(),
			study.getTitle(),
			study.getRecruitmentCount(),
			study.getParticipantsCount(),
			study.getType(),
			tags,
			scrapCount,
			study.getRecruitmentStatus(),
			isScrappedStudy,
			study.getRecruitmentStartAt(),
			study.getRecruitmentEndAt());
	}
}


