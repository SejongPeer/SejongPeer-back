package com.sejong.sejongpeer.domain.study.dto.response;

import java.util.List;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;

public record StudyApplicantsListRespone(
	Long studyId,

	String studyTitle,
	RecruitmentStatus recruitmentStatus,
	Integer recruitmentCount,
	Integer participantsCount,
	List<Applicant> applicants

) {
	public record Applicant(
		String nickname,
		String studentId,
		Integer grade,
		String major,
		StudyMatchingStatus studyMatchingStatus
	) {

	}
	public static StudyApplicantsListRespone of (Study study, List<StudyRelation> studyRelations) {
		List<Applicant> applicants = studyRelations.stream()
			.filter(studyRelation
				-> studyRelation.getStatus().equals(StudyMatchingStatus.PENDING)
				||studyRelation.getStatus().equals(StudyMatchingStatus.ACCEPT)
				||studyRelation.getStatus().equals(StudyMatchingStatus.REJECT))
			.map(studyRelation -> new Applicant(
				studyRelation.getMember().getNickname(),
				studyRelation.getMember().getStudentId(),
				studyRelation.getMember().getGrade(),
				studyRelation.getMember().getCollegeMajor().getMajor(),
				studyRelation.getStatus()
			))
			.toList();

		return new StudyApplicantsListRespone(
			study.getId(),
			study.getTitle(),
			study.getRecruitmentStatus(),
			study.getRecruitmentCount(),
			study.getParticipantsCount(),
			applicants
		);
	}
}
