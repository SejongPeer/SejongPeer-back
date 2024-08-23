package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import org.springframework.data.jpa.domain.Specification;

public class StudySpecification {

	private static final int MININUM_STUDY_PERSONNEL_EXCLUDING_SELF = 1;
	private static final int MAXINUM_STUDY_PERSONNEL_EXCLUDING_SELF = 9;

	public static Specification<Study> checkStudyTypeMatching(StudyType studyType) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("type"), studyType);
	}

	public static Specification<Study> checkRecruitmentPersonnelMatch(Integer recruitmentPersonnel) {
		return (root, query, criteriaBuilder) -> {
			if (recruitmentPersonnel == null) {
				return criteriaBuilder.between(root.get("recruitmentCount"), MININUM_STUDY_PERSONNEL_EXCLUDING_SELF, MAXINUM_STUDY_PERSONNEL_EXCLUDING_SELF);
			} else {
				return criteriaBuilder.equal(root.get("recruitmentCount"), recruitmentPersonnel);
			}
		};
	}

	public static Specification<Study> findByRecruitmentStatus(Boolean isRecruiting) {
		return (root, query, criteriaBuilder) -> {
			if (isRecruiting == null) {
				return criteriaBuilder.or(
					criteriaBuilder.equal(root.get("recruitmentStatus"), RecruitmentStatus.RECRUITING),
					criteriaBuilder.equal(root.get("recruitmentStatus"), RecruitmentStatus.CLOSED)
				);
			} else if (isRecruiting) {
				return criteriaBuilder.equal(root.get("recruitmentStatus"), RecruitmentStatus.RECRUITING);
			} else {
				return criteriaBuilder.equal(root.get("recruitmentStatus"), RecruitmentStatus.CLOSED);
			}
		};
	}

	public static Specification<Study> containsTitleOrContent(String searchWord) {
		return (root, query, criteriaBuilder) -> {
			if (searchWord == null) {
				return criteriaBuilder.conjunction();
			} else {
				return criteriaBuilder.or(
					criteriaBuilder.like(root.get("title"), "%" + searchWord + "%"),
					criteriaBuilder.like(root.get("content"), "%" + searchWord + "%")
				);
			}
		};
	}

}
