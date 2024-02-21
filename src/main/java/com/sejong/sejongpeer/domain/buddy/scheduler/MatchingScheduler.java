package com.sejong.sejongpeer.domain.buddy.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sejong.sejongpeer.domain.buddy.service.MatchingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MatchingScheduler {
	private final MatchingService matchingService;

	// 매 1시간마다 실행
	@Scheduled(cron = "0 0 0/1 * * *")
	public void executeMatchingPeriodically() {
		matchingService.executeMatching();
	}

}
