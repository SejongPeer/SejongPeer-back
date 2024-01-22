package com.sejong.sejongpeer.domain.member.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    ACTIVE("활성화"),
    BLOCKED("비활성화");

    private final String value;
}
