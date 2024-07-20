package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.StudyTagMap;
import com.sejong.sejongpeer.domain.study.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;

public record StudyTotalPostResponse(
	Long id,
	String title,
	String createdAt,
	boolean hasImage,
	String categoryName,
	int scrapCount,
	List<String> tags
) {
	public static StudyTotalPostResponse fromLectureStudy(Study study, String lectureName, int scrapCount) {
		boolean hasImage = study.getImageUrl() != null;
		return new StudyTotalPostResponse(
			study.getId(),
			study.getTitle(),
			study.getCreatedAt().toString().substring(0, 10),
			hasImage,
			lectureName,
			scrapCount,
			study.getStudyTagMaps().stream().map(StudyTagMap::getTag).map(Tag::getName).collect(Collectors.toUnmodifiableList())
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
			scrapCount,
			study.getStudyTagMaps().stream().map(StudyTagMap::getTag).map(Tag::getName).collect(Collectors.toUnmodifiableList())
		);
	}
}

