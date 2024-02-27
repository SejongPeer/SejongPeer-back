package com.sejong.sejongpeer.domain.buddy.dto.request;

import jakarta.validation.constraints.NotNull;

public record MatchingResultRequest(
    @NotNull(message = "짝매칭 수락, 거절에 대해 true, false로 요청해주세요.") boolean isAccept
) { }
