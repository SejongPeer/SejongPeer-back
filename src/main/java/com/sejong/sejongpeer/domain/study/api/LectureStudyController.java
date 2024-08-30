package com.sejong.sejongpeer.domain.study.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.study.dto.request.LectureStudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.service.LectureStudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "5-2. [학교수업 스터디 생성]", description = "학교수업 스터디 게시글 작성 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/study/lecture")
@RequiredArgsConstructor
public class LectureStudyController {

	private final LectureStudyService lectureStudyService;

	@Operation(summary = "학교수업 스터디 생성", description = "학교수업 스터디를 생성합니다.")
	@PostMapping
	public ResponseEntity<StudyCreateResponse> createStudy(
		@Valid @RequestBody LectureStudyCreateRequest request) {
		StudyCreateResponse response = lectureStudyService.createStudy(request);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(response);
	}
}
