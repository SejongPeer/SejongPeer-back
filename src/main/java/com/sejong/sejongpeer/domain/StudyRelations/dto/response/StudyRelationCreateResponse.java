package com.sejong.sejongpeer.domain.StudyRelations.dto.response;

import com.sejong.sejongpeer.domain.StudyRelations.domain.StudyRelation;
import com.sejong.sejongpeer.domain.study.entity.Study;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyRelationCreateResponse(
	@Schema(description = "스터디 ID") Long studyId
) {
	public static StudyRelationCreateResponse from(StudyRelation studyRelation) {
		return new StudyRelationCreateResponse(studyRelation.getStudy().getId());
	}
}
