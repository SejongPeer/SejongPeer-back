package com.sejong.sejongpeer.domain.study.entity;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.lecture.entity.Lecture;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureStudy extends BaseAuditEntity {
	@Id
	private Long studyId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Study study;

	@ManyToOne(fetch = FetchType.LAZY)
	private Lecture lecture;

	@Builder(access = AccessLevel.PRIVATE)
	private LectureStudy(Study study, Lecture lecture) {
		this.study = study;
		this.lecture = lecture;
	}

	public static LectureStudy createLectureStudy(Lecture lecture, Study saveStudy) {
		return LectureStudy.builder()
			.study(saveStudy)
			.lecture(lecture)
			.build();
	}
}
