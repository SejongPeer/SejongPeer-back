package com.sejong.sejongpeer.domain.buddy.service;

import com.sejong.sejongpeer.domain.buddy.dto.request.BuddyRegistrationRequest;
import com.sejong.sejongpeer.domain.buddy.dto.request.MatchingResultRequest;
import com.sejong.sejongpeer.domain.buddy.dto.response.ActiveCustomersCountResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.CompletedPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.buddy.dto.response.MatchingStatusResponse;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BuddyManagementFacade {

	private final BuddyService buddyService;
	private final BuddyMatchingService buddyMatchingService;
	private final MatchingService matchingService;

	public void registerBuddy(BuddyRegistrationRequest request) {
		Buddy newBuddy = buddyService.registerBuddy(request);
		matchingService.matchBuddyWhenRegister(newBuddy);
	}

	public void cancelBuddy() {
		buddyService.cancelBuddy();
	}

	public MatchingStatusResponse getBuddyMatchingStatusAndCount() {
		return buddyService.getBuddyMatchingStatusAndCount();
	}

	public MatchingPartnerInfoResponse getBuddyMatchingPartnerDetails() {
		return buddyService.getBuddyMatchingPartnerDetails();
	}

	public List<CompletedPartnerInfoResponse> getBuddyMatchedPartnerDetails() {
		return buddyService.getBuddyMatchedPartnerDetails();
	}

	public ActiveCustomersCountResponse getCurrentlyActiveBuddyCount() {
		return buddyService.getCurrentlyActiveBuddyCount();
	}

	public void updateBuddyMatchedStatus(MatchingResultRequest request) {
		buddyMatchingService.updateBuddyMatchedStatus(request);
	}
}
