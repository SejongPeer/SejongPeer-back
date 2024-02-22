package com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    IN_PROGRESS("찾는 중"),
    MATCHING_COMPLETED("매칭 성사"),
    MATCHING_FAIL("매칭 실패");
    private final String value;
}
