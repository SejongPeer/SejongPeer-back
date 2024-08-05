package com.sejong.sejongpeer.domain.study.dto.response;

import java.util.List;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;

public record StudyApplicantsListRespone(
	Long studyId,

	String studyTitle,
	String recruitmentStatus,
	Integer recruitmentCount,
	Integer participantsCount,
	List<Applicant> applicants

) {
	public record Applicant(
		String nickname,
		String studentId,
		Integer grade,
		String major,
		String studyMatchingStatus
	) {

	}
	public static StudyApplicantsListRespone of (Study study, List<StudyRelation> studyRelations) {
		List<Applicant> applicantss = studyRelations.stream()
			.map(studyRelation -> new Applicant(
				studyRelation.getMember().getNickname(),
				studyRelation.getMember().getStudentId(),
				studyRelation.getMember().getGrade(),
				studyRelation.getMember().getCollegeMajor().getMajor(),
				studyRelation.getStatus().getValue()
			))
			.toList();

		return new StudyApplicantsListRespone(
			study.getId(),
			study.getTitle(),
			study.getRecruitmentStatus().getValue(),
			study.getRecruitmentCount(),
			study.getParticipantsCount(),
			applicantss
		);
	}
}
