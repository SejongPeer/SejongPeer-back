package com.sejong.sejongpeer.domain.college.api;

import com.sejong.sejongpeer.domain.college.dto.CollegeLessonProfResponse;
import com.sejong.sejongpeer.domain.college.service.CollegeLessonProfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colleges")
@RequiredArgsConstructor
public class CollegeLessonProfController {

	private final CollegeLessonProfService lessonProfService;

	@GetMapping("/lessons-info")
	public List<CollegeLessonProfResponse> getLessonInfoByColleage(
		@RequestParam(name = "college") String college) {
		return lessonProfService.getLessonInfoByColleage(college);
	}
}
