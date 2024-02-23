package com.sejong.sejongpeer.domain.buddy.dto.request;

public record BuddyMatchingStatusUpdateRequest(
	String ownerBuddyId,
	String partnerBuddyId
) {}
