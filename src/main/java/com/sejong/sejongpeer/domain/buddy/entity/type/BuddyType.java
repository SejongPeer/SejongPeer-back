package com.sejong.sejongpeer.domain.buddy.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuddyType {
    SENIOR("선배"),
    JUNIOR("후배"),
    MATE("동기"),
    NO_MATTER("상관없음");
    private final String value;
}
