package com.sejong.sejongpeer.domain.lecture.entity;

import java.util.ArrayList;
import java.util.List;

import com.sejong.sejongpeer.domain.study.entity.LectureStudy;

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

	private String major; // 개설 학과
	private String college; // 단과대

	@OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LectureStudy> lectureStudies = new ArrayList<>();
}
