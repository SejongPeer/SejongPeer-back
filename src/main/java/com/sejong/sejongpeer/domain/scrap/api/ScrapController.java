package com.sejong.sejongpeer.domain.scrap.api;

import com.sejong.sejongpeer.domain.scrap.dto.response.StudyScrapCountResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyTotalPostResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.scrap.application.ScrapService;
import com.sejong.sejongpeer.domain.scrap.dto.request.StudyScrapCreateRequest;
import com.sejong.sejongpeer.domain.scrap.dto.response.StudyScrapResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Tag(name = "8. [스크랩]", description = "스터디 스크랩 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scraps")
public class ScrapController {

	private final ScrapService scrapService;

//	@Operation(summary = "게시글 별 스크랩 수 조회 및 스크랩 여부 판별", description = "특정 스터디 게시글에 사용자가 스크랩을 했는지 여부와 해당 스터디의 총 스크랩 수를 반환합니다.")
//	@GetMapping("/study")
//	public StudyScrapResponse findScrap(
//		@RequestBody StudyScrapCreateRequest request
//	) {
//		return scrapService.findOneStudyScrap(request);
//	}

	@Operation(summary = "게시글 별 스크랩 수 조회", description = "스터디를 스크랩한 유저가 몇 명인지 반환합니다.")
	@GetMapping("/study/{studyId}")
	public StudyScrapCountResponse getScrapCountByStudyPost(@PathVariable Long studyId) {
		return scrapService.getScrapCountByStudyPost(studyId);
	}

	@Operation(summary = "스터디 스크랩 추가", description = "스터디를 스크랩합니다.")
	@PostMapping("/study/{studyId}")
	public Long scrapCreate(
		@PathVariable Long studyId
	) {
		return scrapService.createScrap(studyId);
	}

	@Operation(summary = "스터디 스크랩을 삭제", description = "스터디 스크랩을 삭제합니다.")
	@DeleteMapping("/{scrapId}")
	public void scrapDelete(
		@PathVariable Long scrapId
	) {
		scrapService.deleteScrap(scrapId);
	}

	@Operation(summary = "내가 스크랩한 게시글 목록 조회", description = "내가 좋아요한 교내/교외 스터디 게시글의 목록을 반환합니다.")
	@GetMapping("/all")
	public List<StudyTotalPostResponse> getAllMyScrapStudyPosts() {
		return scrapService.getAllMyScrapStudyPosts();
	}
}
