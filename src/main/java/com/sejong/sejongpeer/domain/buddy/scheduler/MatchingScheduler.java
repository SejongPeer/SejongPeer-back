package com.sejong.sejongpeer.domain.buddy.scheduler;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepository;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.domain.buddy.service.BuddyMatchingService;
import com.sejong.sejongpeer.domain.buddy.service.MatchingService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;

import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchingScheduler {
	private static final int NO_RESPONSE_HOUR_LIMIT = 24;
	private static final String FESTIVAL_START_DATE = "2024-05-29T00:00:00";
	private static final String FESTIVAL_END_DATE = "2024-05-31T23:59:59";
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	private final BuddyRepository buddyRepository;
	private final BuddyMatchedRepository buddyMatchedRepository;
	private final MatchingService matchingService;
	private final BuddyMatchingService buddyMatchingService;

	// 매 1시간마다 실행
	@Scheduled(cron = "0 0 0/1 * * *")
	public void executeMatchingPeriodically() {
		matchingService.executeMatching();
	}

	@Scheduled(cron = "0 0 0 */1 * *")
	public void updateBuddyStatusAutomatically() {
		List<Buddy> foundBuddies = buddyRepository.findAllByStatus(BuddyStatus.FOUND_BUDDY);
		LocalDateTime currentTime = LocalDateTime.now();

		for (Buddy noResposeBuddy : foundBuddies) {
			LocalDateTime updatedTime = noResposeBuddy.getUpdatedAt();
			long hoursElapsed = ChronoUnit.HOURS.between(updatedTime, currentTime);

			if (hoursElapsed >= NO_RESPONSE_HOUR_LIMIT) {
				noResposeBuddy.changeStatus(BuddyStatus.REJECT);
				buddyMatchingService.sendMatchingFailurePenaltyMessage(noResposeBuddy.getMember().getPhoneNumber(),
					SmsText.MATCHING_AUTO_FAILED_REJECT);

				BuddyMatched progressMatch = buddyMatchingService.getBuddyMatchedByBuddy(noResposeBuddy);

				Buddy partnerBuddy = buddyMatchingService.getOtherBuddyInBuddyMatched(progressMatch, noResposeBuddy);
				partnerBuddy.changeStatus(BuddyStatus.DENIED);
				buddyMatchingService.sendMatchingFailurePenaltyMessage(partnerBuddy.getMember().getPhoneNumber(),
					SmsText.MATCHING_AUTO_FAILED_DENIED);

				progressMatch.changeStatus(BuddyMatchedStatus.MATCHING_FAIL);

				buddyMatchedRepository.save(progressMatch);

			}
		}

		buddyRepository.saveAll(foundBuddies);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void resetBuddyStatusDaily() {
		LocalDateTime startDate = LocalDateTime.parse(FESTIVAL_START_DATE, DATE_TIME_FORMATTER);
		LocalDateTime endDate = LocalDateTime.parse(FESTIVAL_END_DATE, DATE_TIME_FORMATTER);
		List<Buddy> buddiesInFestivalPeriod = buddyRepository.findAllByCreatedAtBetween(startDate, endDate);

		for (Buddy buddy : buddiesInFestivalPeriod) {
			buddy.changeStatus(BuddyStatus.REACTIVATE);
		}

		buddyRepository.saveAll(buddiesInFestivalPeriod);
	}
}
