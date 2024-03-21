package com.sejong.sejongpeer.domain.buddy.scheduler;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepository;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.domain.buddy.service.BuddyMatchingService;
import com.sejong.sejongpeer.domain.buddy.service.MatchingService;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.infra.sms.service.SmsService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MatchingScheduler {
	private final BuddyRepository buddyRepository;
	private final BuddyMatchedRepository buddyMatchedRepository;
    private final MatchingService matchingService;
	private final BuddyMatchingService buddyMatchingService;
	private final SmsService smsService;

    // 매 1시간마다 실행
    @Scheduled(cron = "0 0 0/1 * * *")
    public void executeMatchingPeriodically() {
        matchingService.executeMatching();
    }

	@Scheduled(cron = "0 0 0 */1 * *")
	public void autoRejectBuddyRequest() {
		List<Buddy> foundBuddies = buddyRepository.findAllByStatus(BuddyStatus.FOUND_BUDDY);
		LocalDateTime currentTime = LocalDateTime.now();

		for (Buddy NoResposeBuddy : foundBuddies) {
			LocalDateTime createdTime = NoResposeBuddy.getCreatedAt();
			long hoursElapsed = ChronoUnit.HOURS.between(createdTime, currentTime);

			if (hoursElapsed >= 24) {
				NoResposeBuddy.changeStatus(BuddyStatus.REJECT);
				String autoRejectBuddyPhoneNumber = NoResposeBuddy.getMember().getPhoneNumber();
				smsService.sendSms(autoRejectBuddyPhoneNumber, SmsText.MATCHING_AUTO_FAILED_REJECT);

				BuddyMatched progressMatch = buddyMatchedRepository.findByOwnerOrPartnerAndStatus(NoResposeBuddy, BuddyMatchedStatus.IN_PROGRESS)
					.orElseThrow(() -> new CustomException(ErrorCode.TARGET_BUDDY_NOT_FOUND));

				Buddy partnerBuddy = buddyMatchingService.getOtherBuddyInBuddyMatched(progressMatch, NoResposeBuddy);
				partnerBuddy.changeStatus(BuddyStatus.DENIED);
				String autoDeniedBuddyPhoneNumber = partnerBuddy.getMember().getPhoneNumber();
				smsService.sendSms(autoDeniedBuddyPhoneNumber, SmsText.MATCHING_AUTO_FAILED_DENIED);

			}
		}
	}

}
