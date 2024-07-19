package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.study.entity.Study;

public record StudyTotalPostResponse(
	Long id,
	String title,
	String createdAt,
	boolean hasImage,
	String categoryName,
	int scrapCount
) {
	public static StudyTotalPostResponse fromLectureStudy(Study study, String lectureName, int scrapCount) {
		boolean hasImage = study.getImageUrl() != null;
		return new StudyTotalPostResponse(
			study.getId(),
			study.getTitle(),
			study.getCreatedAt().toString().substring(0, 10),
			hasImage,
			lectureName,
			scrapCount
		);
	}

	public static StudyTotalPostResponse fromExternalActivityStudy(Study study, String activityCategoryName, int scrapCount) {
		boolean hasImage = study.getImageUrl() != null;
		return new StudyTotalPostResponse(
			study.getId(),
			study.getTitle(),
			study.getCreatedAt().toString().substring(0, 10),
			hasImage,
			activityCategoryName,
			scrapCount
		);
	}
}

