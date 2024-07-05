package com.sejong.sejongpeer.domain.studyrelation.dto.response;

import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;

import io.swagger.v3.oas.annotations.media.Schema;

public record StudyRelationCreateResponse(@Schema(description = "스터디 지원 ID") Long studyRelationId) {
	public static StudyRelationCreateResponse from(StudyRelation studyRelation) {
		return new StudyRelationCreateResponse(studyRelation.getStudy().getId());
	}
}
