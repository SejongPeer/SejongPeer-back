package com.sejong.sejongpeer.domain.lecturestudy.dto;


import com.sejong.sejongpeer.domain.lecturestudy.entity.Lecture;

public record CollegeLessonProfessorResponse(String lesson, String professor) {
	public static CollegeLessonProfessorResponse from(Lecture lecture) {
		return new CollegeLessonProfessorResponse(
			lecture.getName(),
			lecture.getProfessor()
		);
	}
}
