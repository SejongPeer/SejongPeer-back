package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.StudyTagMap;
import com.sejong.sejongpeer.domain.study.entity.Tag;

import java.util.List;
import java.util.stream.Collectors;

public record StudyTotalPostNoLoginResponse(
	Long id,
	String title,
	String createdAt,
	String recruitmentEndAt,
	boolean hasImage,
	String recruitmentStatus,
	Integer participantCount,
	Integer totalRecruitmentCount,
	String categoryName,
	int scrapCount,
	List<String> tags
) {
	public static StudyTotalPostNoLoginResponse fromLectureStudy(Study study, String lectureName, int scrapCount) {
		boolean hasImage = !study.getImages().isEmpty();
		return new StudyTotalPostNoLoginResponse(
			study.getId(),
			study.getTitle(),
			study.getCreatedAt().toString().substring(0, 10),
			study.getRecruitmentEndAt().toString().substring(0, 10),
			hasImage,
			study.getRecruitmentStatus().getValue(),
			study.getParticipantsCount(),
			study.getRecruitmentCount(),
			lectureName,
			scrapCount,
			study.getStudyTagMaps().stream().map(StudyTagMap::getTag).map(Tag::getName).collect(Collectors.toUnmodifiableList())
		);
	}

	public static StudyTotalPostNoLoginResponse fromExternalActivityStudy(Study study, String activityCategoryName, int scrapCount) {
		boolean hasImage = !study.getImages().isEmpty();
		return new StudyTotalPostNoLoginResponse(
			study.getId(),
			study.getTitle(),
			study.getCreatedAt().toString().substring(0, 10),
			study.getRecruitmentEndAt().toString().substring(0, 10),
			hasImage,
			study.getRecruitmentStatus().getValue(),
			study.getParticipantsCount(),
			study.getRecruitmentCount(),
			activityCategoryName,
			scrapCount,
			study.getStudyTagMaps().stream().map(StudyTagMap::getTag).map(Tag::getName).collect(Collectors.toUnmodifiableList())
		);
	}
}


