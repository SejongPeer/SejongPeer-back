package com.sejong.sejongpeer.domain.study.api;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.service.StudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "3. [스터디]", description = "스터디 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class StudyController {
	private final StudyService studyService;

	@Operation(summary = "스터디 수정", description = "스터디를 수정합니다.")
	@PatchMapping("/{studyId}")
	public void updateStudy(
		@Valid @RequestBody StudyUpdateRequest request, @PathVariable Long studyId) {
		studyService.updateStudy(request, studyId);
	}
}
