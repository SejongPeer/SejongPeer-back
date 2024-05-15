package com.sejong.sejongpeer.domain.honbab.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.buddy.dto.response.ActiveCustomersCountResponse;
import com.sejong.sejongpeer.domain.honbab.dto.request.RegisterHonbabRequest;
import com.sejong.sejongpeer.domain.honbab.dto.response.HonbabMatchingStatusResponse;
import com.sejong.sejongpeer.domain.honbab.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.honbab.service.HonbabService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "6. [혼밥]", description = "세종혼밥짝꿍 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/honbab")
public class HonbabController {
	private final HonbabService honbabService;

	@Operation(summary = "혼밥등록", description = "유저가 혼밥 등록")
	@PostMapping("/register")
	public void registerHonbab(@Valid @RequestBody RegisterHonbabRequest request) {
		honbabService.registerHonbab(request);
	}

	@Operation(summary = "혼밥 매칭 상태 체크", description = "유저의 가장 최근 혼밥 상태값 리턴")
	@GetMapping("/check-matching-status")
	public HonbabMatchingStatusResponse getHonbabMatchingStatus() {
		return honbabService.getHonbabMatchingStatus();
	}

	@Operation(summary = "혼밥 중도 취소", description = "혼밥신청 후 취소")
	@GetMapping("/cancel")
	public void cancelHonbab() {
		honbabService.cancelHonbab();
	}

	@Operation(summary = "혼밥짝꿍 찾은 후 상대방 정보 요청", description = "매칭 후 상대방의 정보 반환")
	@GetMapping("/partner/information")
	public MatchingPartnerInfoResponse getFoundPartnerInfo() {
		return honbabService.getPartnerInfo();
	}

	@Operation(summary = "혼밥 상대를 찾고있는 이용자 수 요청", description = "IN_PROGRESS인 상태인 혼밥 이용자")
	@GetMapping("/active-count")
	public ActiveCustomersCountResponse getCurrentlyActiveHonbabCount() {
		return honbabService.getCurrentlyActiveHonbabCount();
	}
}
