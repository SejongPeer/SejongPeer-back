package com.sejong.sejongpeer.domain.buddy.dto.request;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;

import jakarta.validation.constraints.NotNull;

public record MatchingResultRequest(
    @NotNull(message = "ACCEPT 또는 REJECT를 선택해주세요.") BuddyStatus buddyStatus
) { }
