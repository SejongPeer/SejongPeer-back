package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.image.dto.response.StudyImageUrlResponse;
import com.sejong.sejongpeer.domain.study.entity.Study;

import java.util.List;
import java.util.stream.Collectors;

public record StudyPostInfoResponse(
	String title,
	String writerMajor,
	String writerNickname,
	String recruitmentStart,
	String recruitmentEnd,
	String content,
	String categoryName,
	Integer participantCount,
	Integer totalRecruitmentCount,
	String studyFrequency,
	String studyMethod,
	String questionKakaoLink,
	List<StudyImageUrlResponse> imgUrlList
) {
	public static StudyPostInfoResponse fromStudy(Study study, String categoryName) {
		return new StudyPostInfoResponse(
			study.getTitle(),
			study.getMember().getCollegeMajor().getMajor(),
			study.getMember().getNickname(),
			study.getRecruitmentStartAt().toString().substring(0, 10),
			study.getRecruitmentEndAt().toString().substring(0, 10),
			study.getContent(),
			categoryName,
			study.getParticipantsCount(),
			study.getRecruitmentCount(),
			study.getFrequency().getValue(),
			study.getMethod().getValue(),
			study.getQuestionLink(),
			study.getImages().stream().map(StudyImageUrlResponse::fromImage).collect(Collectors.toUnmodifiableList())
		);
	}

	;
}
