package com.sejong.sejongpeer.domain.honbab.api;

import com.sejong.sejongpeer.domain.honbab.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.honbab.service.HonbabService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;
@Tag(name = "6. [혼밥]", description = "세종혼밥짝꿍 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/honbab")
public class HonbabController {

	private final HonbabService honbabService;

	@Operation(summary = "혼밥짝꿍 찾은 후 상대방 정보 요청", description = "매칭 후 상대방의 정보 반환")
	@GetMapping("/partner/information")
	public MatchingPartnerInfoResponse getFoundPartnerInfo() {
		String memberId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return honbabService.getPartnerInfo(memberId);
	}
}
