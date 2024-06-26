package com.sejong.sejongpeer.domain.lecturestudy.dto;


import com.sejong.sejongpeer.domain.lecturestudy.entity.Lecture;

public record CollegeLessonProfessorResponse(Long lessonId, String lesson, String professor) {
	public static CollegeLessonProfessorResponse from(Lecture lecture) {
		return new CollegeLessonProfessorResponse(
			lecture.getId(),
			lecture.getName(),
			lecture.getProfessor()
		);
	}
}
