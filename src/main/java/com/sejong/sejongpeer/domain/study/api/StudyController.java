package com.sejong.sejongpeer.domain.study.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.service.StudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. [스터디]", description = "스터디 관련 API입니다.")
@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

	private final StudyService studyService;

	@Operation(summary = "스터디 생성", description = "스터디를 생성합니다.")
	@PostMapping
	public ResponseEntity<StudyCreateResponse> studyCreate(@Valid @RequestBody StudyCreateRequest studyCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(studyService.createStudy(studyCreateRequest));
	}
}
