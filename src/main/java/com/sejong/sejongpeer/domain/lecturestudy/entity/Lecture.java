package com.sejong.sejongpeer.domain.lecturestudy.entity;

import java.util.ArrayList;
import java.util.List;

import com.sejong.sejongpeer.domain.common.BaseEntity;
import com.sejong.sejongpeer.domain.externalactivitystudy.entity.ExternalActivityStudy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String professor;

	private String code; // 학수번호

	private Integer classNo; // 분반

	@OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LectureStudy> lectureStudies = new ArrayList<>();

	private String college; // 단과대
}
