package com.sejong.sejongpeer.domain.scrap.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.scrap.application.ScrapService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scraps")
public class ScrapController {

	private final ScrapService scrapService;

	@Operation(summary = "스터디 스크랩 추가", description = "스터디를 스크랩합니다.")
	@PostMapping("/study/{studyId}")
	public Long scrapCreate(
		@PathVariable Long studyId
	) {
		return scrapService.createScrap(studyId);
	}
}
