package com.sejong.sejongpeer.domain.buddy.api;

import com.sejong.sejongpeer.domain.buddy.dto.request.MatchingResultRequest;
import com.sejong.sejongpeer.domain.buddy.service.BuddyMatchingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "5. [버디 매칭]", description = "세종버디 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buddyMatching")
public class BuddyMatchingController {
	private final BuddyMatchingService buddyMatchingService;

	@Operation(summary = "버디 매칭 상태 업데이트", description = "버디 매칭 상태 관리")
	@PostMapping("/status")
	public void checkBuddyMatching(@Valid @RequestBody MatchingResultRequest request) {
		String memberId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		buddyMatchingService.updateBuddyMatchingStatus(memberId, request);
	}
}
