package com.sejong.sejongpeer.domain.study.dto.response;

import java.util.List;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;

public record StudyApplicantsListRespone(
	Long studyId,

	String studyTitle,
	List<Applicant> applicants

) {
	public record Applicant(
		String nickname,
		String studentId,
		Integer grade,
		String major
	) {

	}
	public static StudyApplicantsListRespone of (Study study, List<Member> members) {
		List<Applicant> applicantss = members.stream()
			.map(member -> new Applicant(
				member.getNickname(),
				member.getStudentId(),
				member.getGrade(),
				member.getCollegeMajor().getMajor()
			))
			.toList();

		return new StudyApplicantsListRespone(
			study.getId(),
			study.getTitle(),
			applicantss
		);
	}
}
