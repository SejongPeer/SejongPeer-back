package com.sejong.sejongpeer.domain.studyRelations.dto.response;

import com.sejong.sejongpeer.domain.studyRelations.domain.StudyRelation;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyRelationCreateResponse(@Schema(description = "스터디 ID") Long studyId) {
	public static StudyRelationCreateResponse from(StudyRelation studyRelation) {
		return new StudyRelationCreateResponse(studyRelation.getStudy().getId());
	}
}
