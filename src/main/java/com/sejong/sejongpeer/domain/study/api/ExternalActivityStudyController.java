package com.sejong.sejongpeer.domain.study.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.study.dto.request.ExternalActivityStudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.service.ExternalActivityStudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "3-2. [대외활동 스터디]", description = "대외활동 스터디 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study/external-activity")
public class ExternalActivityStudyController {
	private final ExternalActivityStudyService externalActivityStudyService;

	@Operation(summary = "외부 활동 스터디 생성", description = "외부 활동 스터디를 생성합니다.")
	@PostMapping
	public ResponseEntity<StudyCreateResponse> createStudy(
		@Valid @RequestBody ExternalActivityStudyCreateRequest request) {
		StudyCreateResponse response = externalActivityStudyService.createStudy(request);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(response);
	}
}
