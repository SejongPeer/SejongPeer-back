package com.sejong.sejongpeer.domain.buddy.api;

import com.sejong.sejongpeer.domain.buddy.dto.request.RegisterRequest;
import com.sejong.sejongpeer.domain.buddy.service.BuddyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
			(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		buddyService.registerBuddy(request, memberId);
	}

	@Operation(summary = "버디 중도 취소", description = "매칭 전 버디 취소")
	@PostMapping("/cancel")
	public void cancelBuddy(@Valid @RequestBody RegisterRequest request) {
		String memberId =
			(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		buddyService.cancelBuddy(memberId);
	}
}
