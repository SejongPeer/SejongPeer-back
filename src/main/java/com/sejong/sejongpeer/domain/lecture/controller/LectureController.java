package com.sejong.sejongpeer.domain.lecture.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.lecture.dto.CollegeLectureProfessorResponse;
import com.sejong.sejongpeer.domain.lecture.service.LectureService;

@Tag(name = "5. [수업]", description = "수업 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/colleges")
@RequiredArgsConstructor
public class LectureController {

	private final LectureService lectureService;

	@Operation(summary = "수업 정보 조회", description = "스터디 게시글 작성에 필요한 단과대 별 개설된 수업 정보를 반환합니다.")
	@GetMapping("/lectures")
	public List<CollegeLectureProfessorResponse> getLectureInfoByCollege(
		@RequestParam(name = "college") String college) {

		return lectureService.getLessonInfoByColleage(college);
	}
}
