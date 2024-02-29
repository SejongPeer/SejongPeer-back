package com.sejong.sejongpeer.domain.buddy.dto.response;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;

public record MatchingStatusResponse(BuddyStatus status) {
	public static MatchingStatusResponse buddyFrom(Buddy buddy) {
		return new MatchingStatusResponse(buddy.getStatus());
	}
}
