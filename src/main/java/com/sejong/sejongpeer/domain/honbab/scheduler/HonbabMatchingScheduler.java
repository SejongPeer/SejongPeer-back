package com.sejong.sejongpeer.domain.honbab.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sejong.sejongpeer.domain.honbab.service.HonbabMatchingService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HonbabMatchingScheduler {
	private final HonbabMatchingService honbabMatchingService;

	// 매 1분마다 실행
	@Scheduled(cron = "0 0/1 * * * *")
	public void executeMatchingPeriodically() {
		honbabMatchingService.executeMatching();
	}
}
