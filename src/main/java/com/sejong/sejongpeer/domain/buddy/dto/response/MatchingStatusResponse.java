package com.sejong.sejongpeer.domain.buddy.dto.response;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;

public record MatchingStatusResponse(BuddyStatus status, Long matchingCompletedCount) {
	public static MatchingStatusResponse buddyFrom(Buddy buddy, Long matchingCompletedCount) {
		return new MatchingStatusResponse(buddy.getStatus(), matchingCompletedCount);
	}
}
