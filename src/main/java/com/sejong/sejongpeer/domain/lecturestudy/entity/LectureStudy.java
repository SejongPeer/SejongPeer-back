package com.sejong.sejongpeer.domain.lecturestudy.entity;

import com.sejong.sejongpeer.domain.study.entity.Study;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LectureStudy {
	@Id
	private String studyId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	private Study study;

	@ManyToOne(fetch = FetchType.LAZY)
	private Lecture lecture;
}
