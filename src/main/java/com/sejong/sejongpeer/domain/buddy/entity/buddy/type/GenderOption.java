package com.sejong.sejongpeer.domain.buddy.entity.buddy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderOption {
    SAME("동일한 성별"),
    NO_MATTER("상관없음");
    private final String value;
}
