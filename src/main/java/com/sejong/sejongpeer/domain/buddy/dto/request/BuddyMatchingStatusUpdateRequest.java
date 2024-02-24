package com.sejong.sejongpeer.domain.buddy.dto.request;


import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;

public record BuddyMatchingStatusUpdateRequest(
	BuddyStatus buddyStatus
) {}

