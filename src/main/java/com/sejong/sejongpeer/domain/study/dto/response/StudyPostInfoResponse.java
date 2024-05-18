package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;

public record StudyPostInfoResponse(
	String title,
	String writerMajor,
	String writerNickname,
	String recruitmentStart,
	String recruitmentEnd,
	String content,
	String categoryName
) {
	public static StudyPostInfoResponse fromStudyAndMember(Study study, Member member, String categoryName) {
		return new StudyPostInfoResponse(
			study.getTitle(),
			member.getCollegeMajor().getMajor(),
			member.getNickname(),
			study.getRecruitmentStartAt().toString().substring(0, 10),
			study.getRecruitmentEndAt().toString().substring(0, 10),
			study.getContent(),
			categoryName
		);
	}

	;
}
