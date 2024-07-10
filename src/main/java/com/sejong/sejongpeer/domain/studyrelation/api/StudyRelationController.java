package com.sejong.sejongpeer.domain.studyrelation.api;

import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyApplyRequest;
import com.sejong.sejongpeer.domain.studyrelation.dto.response.StudyRelationCreateResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sejong.sejongpeer.domain.studyrelation.service.StudyRelationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "7-2. [스터디 신청현황]", description = "스터디 신청 현황 API입니다.")
@RestController
@RequestMapping("/api/v1/study/relations")
@RequiredArgsConstructor
public class StudyRelationController {

	private final StudyRelationService studyRelationService;

	@Operation(summary = "스터디 지원", description = "스터디를 지원합니다.")
	@PostMapping
	public ResponseEntity<StudyRelationCreateResponse> createStudyRelation(@Valid @RequestBody StudyApplyRequest studyApplyRequest) {
		StudyRelationCreateResponse response = studyRelationService.applyStudy(studyApplyRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(response);
	}

	@Operation(summary = "스터디 취소", description = "스터디 신청을 취소합니다.")
	@DeleteMapping
	public ResponseEntity<?> deleteStudyRelation() {
		return null;
	}

	@Operation(summary = "스터디 수락", description = "스터디 신청을 수락합니다.")
	@PutMapping("/accept")
	public ResponseEntity<?> acceptStudyRelation() {
		return null;
	}

	@Operation(summary = "스터디 거부", description = "스터디 신청을 거부합니다.")
	@PutMapping("/reject")
	public ResponseEntity<?> rejectStudyRelation() {
		return null;
	}
}
