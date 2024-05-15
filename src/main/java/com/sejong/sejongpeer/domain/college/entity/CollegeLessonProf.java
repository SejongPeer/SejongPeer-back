package com.sejong.sejongpeer.domain.college.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "college-lesson-prof-relations")
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
