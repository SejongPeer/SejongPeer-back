package com.sejong.sejongpeer.domain.member.dto.response;

public record AccountFindResponse(String account) {
    public static AccountFindResponse of(String account) {
        return new AccountFindResponse(account);
    }
}
