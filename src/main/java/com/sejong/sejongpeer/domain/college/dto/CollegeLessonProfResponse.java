package com.sejong.sejongpeer.domain.college.dto;

import com.sejong.sejongpeer.domain.college.entity.CollegeLessonProf;

public record CollegeLessonProfResponse(String lesson, String prof) {
	public static CollegeLessonProfResponse from(CollegeLessonProf collegeLessonProf) {
		return new CollegeLessonProfResponse(
			collegeLessonProf.getLesson(),
			collegeLessonProf.getProf()
		);
	}
}
