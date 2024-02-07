package com.sejong.sejongpeer.domain.member.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountFindOption {
    PHONE_NUMBER("휴대폰 번호 인증"),
    NAME("실명 인증");

    private final String value;
}
