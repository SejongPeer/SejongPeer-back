package com.sejong.sejongpeer.domain.study.api;

import com.sejong.sejongpeer.domain.study.dto.response.StudyPostInfoNoLoginResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyPostInfoResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyTotalPostNoLoginResponse;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.domain.study.service.StudyNoLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@Tag(name = "5-3. [비로그인 유저를 위한 스터디]", description = "로그인하지 않은 유저를 위한 게시글 조회 API입니다.")
@RestController
@RequestMapping("/api/v1/study/unauthenticated")
@RequiredArgsConstructor
public class StudyNoLoginController {

	private final StudyNoLoginService studyNoLoginService;

	@Operation(summary = "게시글 목록 조회", description = "토큰 없이 게시글 전체 목록을 조회합니다.")
	@GetMapping("/post")
	public Slice<StudyTotalPostNoLoginResponse> getAllStudyPostWithoutLogin(
		@RequestParam(name = "studyType") StudyType studyType,
		@RequestParam(defaultValue = "0") int page) {
		return studyNoLoginService.getAllStudyPostWithoutLogin(studyType, page);
	}

	@Operation(summary = "게시글 단건 상세 조회", description = "토큰 없이 게시글 상세 정보를 조회합니다.")
	@GetMapping("/post/{studyId}")
	public StudyPostInfoNoLoginResponse getOneStudyPostInfo(@PathVariable Long studyId) {
		return studyNoLoginService.getOneStudyPostInfoWithoutLogin(studyId);
	}
}
