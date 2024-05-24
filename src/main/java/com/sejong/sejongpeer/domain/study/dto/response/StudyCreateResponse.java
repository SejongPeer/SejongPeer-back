package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.study.entity.Study;

public record StudyCreateResponse(
	Long id
) {
	public static StudyCreateResponse from(Study study) {
		return new StudyCreateResponse(study.getId());
	}
}
