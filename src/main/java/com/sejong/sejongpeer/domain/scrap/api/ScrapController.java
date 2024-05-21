package com.sejong.sejongpeer.domain.scrap.api;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.scrap.application.ScrapService;
import com.sejong.sejongpeer.domain.scrap.dto.response.StudyScrapResponse;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scraps")
public class ScrapController {

	private final ScrapService scrapService;

	@Operation(summary = "스터디 스크랩 조회", description = "스터디를 스크랩한 정보를 조회합니다.")
	@GetMapping("/study/{studyId}")
	public StudyScrapResponse findScrap(
		@PathVariable Long studyId
	) {
		return scrapService.findOneStudyScrap(studyId);
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
}
