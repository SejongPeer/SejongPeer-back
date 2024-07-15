package com.sejong.sejongpeer.domain.studyrelation.api;

import java.util.List;
import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyMatchingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyApplyRequest;
import com.sejong.sejongpeer.domain.studyrelation.dto.response.AppliedStudyResponse;
import com.sejong.sejongpeer.domain.studyrelation.dto.response.StudyRelationCreateResponse;
import com.sejong.sejongpeer.domain.studyrelation.service.StudyRelationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

	@Operation(summary = "스터디 지원자 수락/거절", description = "자신이 개설한 스터디 지원자의 스터디 신청에 대해 수락/거절을 처리합니다.")
	@PatchMapping("/matching/status")
	public void processStudyMatchingStatus(@Valid @RequestBody StudyMatchingRequest request) {
		studyRelationService.updateStudyMatchingStatus(request);
	}

	@Operation(summary = "스터디 조기마감", description = "스터디 신청을 조기마감 시킵니다.")
	@PatchMapping("/{studyId}/early-close")
	public void earlyCloseRegistration(@PathVariable Long studyId) {
		studyRelationService.earlyCloseRegistration(studyId);
	}

	@Operation(summary = "지원한 스터디 조회", description = "지원한 스터디를 전부 조회합니다")
	@GetMapping("/applied")
	public List<AppliedStudyResponse> getAllAppliedStudies() {
		return 	studyRelationService.getAppliedStudies();
	}
}
