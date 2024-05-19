package com.sejong.sejongpeer.domain.lecture.dto;

import com.sejong.sejongpeer.domain.lecture.entity.Lecture;

public record CollegeLectureProfessorResponse(Long id, String lectureName, String professor) {
	public static CollegeLectureProfessorResponse from(Lecture lecture) {
		return new CollegeLectureProfessorResponse(
			lecture.getId(),
			lecture.getName(),
			lecture.getProfessor()
		);
	}
}
