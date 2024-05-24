package com.sejong.sejongpeer.domain.study.api;

import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudySearchRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.*;
import com.sejong.sejongpeer.domain.study.service.StudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
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

import java.util.List;



@Tag(name = "7-1. [스터디]", description = "스터디 관련 API입니다.")
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

	@Operation(summary = "게시글 목록 조회", description = "학교 수업 스터디 혹은 수업 외 활동 게시글 전체 목록을 반환합니다.")
	@GetMapping("/post/all")
	public Slice<StudyTotalPostResponse> getAllStudyPost(
		@RequestParam(name = "choice") String choice,
		@RequestParam(defaultValue = "0") int page) {
		return studyService.getAllStudyPost(choice, page);
	}

	@Operation(summary = "게시글 단건 상세 조회", description = "게시글 목록 조회에서 리턴 받은 각 게시글 id에 해당하는 게시글의 세부 정보를 반환합니다.")
	@GetMapping("/post/{studyId}")
	public StudyPostInfoResponse getOneStudyPostInfo(@PathVariable Long studyId) {
		return studyService.getOneStudyPostInfo(studyId);
	}

	@Operation(summary = "게시글 검색", description = "검색 조건에 해당하는 모든 게시글을 반환합니다.")
	@GetMapping("/post/search")
	public List<StudyTotalPostResponse> getAllStudyPostBySearch(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@RequestBody StudySearchRequest studySearchRequest) {
		return studyService.getAllStudyPostBySearch(page, size, studySearchRequest);
	}


}
