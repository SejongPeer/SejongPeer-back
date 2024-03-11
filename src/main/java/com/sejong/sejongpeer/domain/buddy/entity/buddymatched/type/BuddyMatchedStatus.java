package com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuddyMatchedStatus {
	IN_PROGRESS("매칭 수락 대기 중"),
	MATCHING_COMPLETED("매칭 성사"),
	MATCHING_FAIL("매칭 실패");
	private final String value;
}
