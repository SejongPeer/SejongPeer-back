package com.sejong.sejongpeer.domain.buddy.api;

import com.sejong.sejongpeer.domain.buddy.dto.request.RegisterRequest;
import com.sejong.sejongpeer.domain.buddy.dto.response.CompletedPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingStatusResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.ActiveCustomersCountResponse;
import com.sejong.sejongpeer.domain.buddy.service.BuddyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "4. [버디]", description = "세종버디 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buddy")
public class BuddyController {
	private final BuddyService buddyService;

	@Operation(summary = "버디등록", description = "유저가 버디에 등록")
	@PostMapping("/register")
	public void registerBuddy(@Valid @RequestBody RegisterRequest request) {
		String memberId =
			(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		buddyService.registerBuddy(request, memberId);
	}

	@Operation(summary = "버디 중도 취소", description = "매칭 전 버디 취소")
	@GetMapping("/cancel")
	public void cancelBuddy() {
		String memberId =
			(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		buddyService.cancelBuddy(memberId);
	}

	@Operation(summary = "버디 매칭 상태 체크", description = "유저의 가장 최근 버디 상태값 리턴")
	@GetMapping("/check-matching-status")
	public MatchingStatusResponse getMatchingStatus() {
		String memberId =
			(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return buddyService.getBuddyMatchingStatusAndCount(memberId);
	}

	@Operation(summary = "버디 수락/거절 창에서의 상대방 정보 요청", description = "매칭 후 상대방의 학과, 학년 정보 리턴")
	@GetMapping("/partner/details")
	public MatchingPartnerInfoResponse getPartnerDetails() {
		String memberId =
			(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return buddyService.getBuddyMatchingPartnerDetails(memberId);
	}

	@Operation(summary = "매칭 수락 완료 후 상대방 정보요청", description = "이름, 학년, 학과, 카카오톡 정보 리턴")
	@GetMapping("/matched/partner/details")
	public List<CompletedPartnerInfoResponse> getBuddyMatchedPartnerDetails() {
		String memberId =
			(String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return buddyService.getBuddyMatchedPartnerDetails(memberId);
	}

	@Operation(summary = "현재 버디를 신청한 이용자 수 요청", description = "IN_PROGRESS, FOUND_BUDDY 상태값인 버디의 수")
	@GetMapping("/active-count")
	public ActiveCustomersCountResponse getCurrentlyActiveBuddyCount() {

		return buddyService.getCurrentlyActiveBuddyCount();
	}
}


