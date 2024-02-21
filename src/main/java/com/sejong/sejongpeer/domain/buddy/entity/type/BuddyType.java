package com.sejong.sejongpeer.domain.buddy.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuddyType {
    SENIOR("시니어"),
    JUNIOR("주니어"),
    MATE("메이트"),
    NO_MATTER("상관없음");
    private final String value;
}
