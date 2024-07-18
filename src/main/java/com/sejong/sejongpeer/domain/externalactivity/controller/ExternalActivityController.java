package com.sejong.sejongpeer.domain.externalactivity.controller;

import com.sejong.sejongpeer.domain.externalactivity.dto.ExternalActivityCategoryResponse;
import com.sejong.sejongpeer.domain.externalactivity.service.ExternalActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "5-1. [교외 활동 종류 정보]", description = "교외 활동 카테고리 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/external-activity")
@RequiredArgsConstructor
public class ExternalActivityController {

	private final ExternalActivityService externalActivityService;

	@Operation(summary = "교외 활동 종류 조회", description = "스터디 게시글 작성에 필요한 교외 활동 카테고리 정보를 반환합니다.")
	@GetMapping()
	public List<ExternalActivityCategoryResponse> getAllExternalActivityCategories() {
		return externalActivityService.getAllExternalActivityCategories();
	}
}
