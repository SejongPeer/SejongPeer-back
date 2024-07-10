package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class StudySpecification {

	public static Specification<Study> checkBiggerThanRecruitmentMin(Integer recruitmentMin) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.greaterThanOrEqualTo(root.get("recruitmentCount"), recruitmentMin);
	}

	public static Specification<Study> checkSmallerThanRecruitmentMax(Integer recruitmentMax) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.lessThanOrEqualTo(root.get("recruitmentCount"), recruitmentMax);
	}

	public static Specification<Study> checkAfterStartedAt(LocalDateTime recruitmentStartAt) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.greaterThanOrEqualTo(root.get("recruitmentStartAt"), recruitmentStartAt);
	}

	public static Specification<Study> checkBeforeClosedAt(LocalDateTime recruitmentEndAt) {
		return (root, query, CriteriaBuilder) -> CriteriaBuilder.lessThanOrEqualTo(root.get("recruitmentEndAt"), recruitmentEndAt);
	}

	public static Specification<Study> findByRecruitmentStatus(Boolean isRecruiting) {
		if (isRecruiting) {
			return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("recruitmentStatus"), RecruitmentStatus.RECRUITING);
		} else {
			return (root, query, CriteriaBuilder) -> CriteriaBuilder.equal(root.get("recruitmentStatus"), RecruitmentStatus.CLOSED);
		}
	}

	public static Specification<Study> containsTitleOrContent(String searchWord) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.or(
			criteriaBuilder.like(root.get("title"), "%" + searchWord + "%"),
			criteriaBuilder.like(root.get("content"), "%" + searchWord + "%")
		);
	}
}
