package com.sejong.sejongpeer.domain.study.api;

import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyFindResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyUpdateResponse;
import com.sejong.sejongpeer.domain.study.service.StudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "3. [스터디]", description = "스터디 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/study")
@RequiredArgsConstructor
public class StudyController {

	private final StudyService studyService;

	@Operation(summary = "스터디 생성", description = "스터디를 생성합니다.")
	@PostMapping
	public ResponseEntity<StudyCreateResponse> studyCreate(
		@Valid @RequestBody StudyCreateRequest studyCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(studyService.createStudy(studyCreateRequest));
	}

	@Operation(summary = "스터디 단건 조회", description = "스터디 한 개를 조회합니다.")
	@GetMapping("/{studyId}")
	public StudyFindResponse studyFindOne(@PathVariable Long studyId) {
		return studyService.findOneStudy(studyId);
	}

	@Operation(summary = "스터디 리스트 조회", description = "스터디 리스트를 조회합니다.")
	@GetMapping
	public Slice<StudyFindResponse> studyFindSlice(
		@RequestParam int size, @RequestParam(required = false) Long lastId) {
		return studyService.findSliceStudy(size, lastId);
	}

	@Operation(summary = "스터디 단건 수정", description = "스터디 한 개를 수정합니다.")
	@PutMapping("/{studyId}")
	public StudyUpdateResponse studyUpdate(
		@Valid @RequestBody StudyUpdateRequest studyUpdateRequest, @PathVariable Long studyId) {
		return studyService.updateStudy(studyUpdateRequest, studyId);
	}

	@Operation(summary = "스터디 단건 삭제", description = "스터디 한 개를 삭제합니다.")
	@DeleteMapping("/{studyId}")
	public void studyDelete(@PathVariable Long studyId) {
		studyService.deleteStudy(studyId);
	}
}
