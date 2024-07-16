package com.sejong.sejongpeer.domain.study.api;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.study.dto.request.LectureStudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyFindResponse;
import com.sejong.sejongpeer.domain.study.service.LectureStudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "3-1. [학교수업 스터디]", description = "학교수업 스터디 관련 API입니다.")
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

	@Operation(summary = "스터디 단건 조회", description = "스터디 한 개를 조회합니다.")
	@GetMapping("/{studyId}")
	public StudyFindResponse studyFindOne(@PathVariable Long studyId) {
		return lectureStudyService.findOneStudy(studyId);
	}

//	@Operation(summary = "스터디 리스트 조회", description = "스터디 리스트를 조회합니다.")
//	@GetMapping
//	public Slice<StudyFindResponse> studyFindSlice(
//		@RequestParam int size, @RequestParam(required = false) Long lastId) {
//		return lectureStudyService.findSliceStudy(size, lastId);
//	}
}
