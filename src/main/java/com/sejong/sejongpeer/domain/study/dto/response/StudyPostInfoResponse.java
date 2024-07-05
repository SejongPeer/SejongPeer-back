package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.study.entity.Study;

public record StudyPostInfoResponse(
	String title,
	String writerMajor,
	String writerNickname,
	String recruitmentStart,
	String recruitmentEnd,
	String content,
	String categoryName,
	Long numberOfApplicants,
	String imgUrl
) {
	public static StudyPostInfoResponse fromStudy(Study study, String categoryName, Long numberOfApplicants) {
		return new StudyPostInfoResponse(
			study.getTitle(),
			study.getMember().getCollegeMajor().getMajor(),
			study.getMember().getNickname(),
			study.getRecruitmentStartAt().toString().substring(0, 10),
			study.getRecruitmentEndAt().toString().substring(0, 10),
			study.getContent(),
			categoryName,
			numberOfApplicants,
			study.getImageUrl()
		);
	}

	;
}
