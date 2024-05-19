package com.sejong.sejongpeer.domain.college.entity;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "college-lectureName-prof-relations")
public class CollegeLessonProf {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("단과대 이름")
	@Column(nullable = false)
	private String college;

	@Comment("수업 이름")
	@Column(nullable = false)
	private String lesson;

	@Comment("교수님 존함")
	private String prof;

	@Builder(access = AccessLevel.PRIVATE)
	private CollegeLessonProf(String college, String lesson, String prof) {
		this.college = college;
		this.lesson = lesson;
		this.prof = prof;
	}
}
