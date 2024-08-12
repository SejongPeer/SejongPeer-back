package com.sejong.sejongpeer.domain.study.api;

import java.util.List;

import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import com.sejong.sejongpeer.domain.study.dto.request.StudyPostSearchRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyPostInfoResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyTotalPostResponse;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.domain.study.service.StudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "5. [스터디]", description = "스터디 관련 API입니다.")
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

	@Operation(summary = "스터디 단건 삭제", description = "스터디 게시글 한 개를 삭제합니다.")
	@DeleteMapping("/{studyId}")
	public void deleteStudy(@PathVariable Long studyId) {
		studyService.deleteStudy(studyId);
	}

	@Operation(summary = "게시글 목록 조회", description = "학교 수업 스터디 혹은 수업 외 활동 게시글 전체 목록을 반환합니다.")
	@GetMapping("/post")
	public Slice<StudyTotalPostResponse> getAllStudyPost(
		@RequestParam(name = "studyType") StudyType studyType,
		@RequestParam(defaultValue = "0") int page) {
		return studyService.getAllStudyPost(studyType, page);
	}

	@Operation(summary = "게시글 단건 상세 조회", description = "게시글 목록 조회에서 리턴 받은 각 게시글 id에 해당하는 게시글의 세부 정보를 반환합니다.")
	@GetMapping("/post/{studyId}")
	public StudyPostInfoResponse getOneStudyPostInfo(@PathVariable Long studyId) {
		return studyService.getOneStudyPostInfo(studyId);
	}

	@Operation(summary = "게시글 검색", description = "검색 조건에 해당하는 모든 게시글을 반환합니다.")
	@GetMapping("/post/search")
	public List<StudyTotalPostResponse> searchPosts(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "20") int size,
		@Valid @RequestBody StudyPostSearchRequest studyPostSearchRequest) {
		return studyService.getAllStudyPostBySearch(page, size, studyPostSearchRequest);
	}
}
