package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.image.dto.response.StudyImageUrlResponse;
import com.sejong.sejongpeer.domain.image.entity.Image;
import com.sejong.sejongpeer.domain.study.entity.Study;

import java.util.List;
import java.util.stream.Collectors;

public record StudyCreateResponse(
	Long id,
	List<StudyImageUrlResponse> imgUrlsList
) {
	public static StudyCreateResponse from(Study study, List<StudyImageUrlResponse> imgUrlsList) {
		return new StudyCreateResponse(
			study.getId(),
			imgUrlsList
		);
	}
}
