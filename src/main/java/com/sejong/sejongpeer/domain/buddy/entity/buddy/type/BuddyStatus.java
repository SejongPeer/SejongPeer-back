package com.sejong.sejongpeer.domain.buddy.entity.buddy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BuddyStatus {
    IN_PROGRESS("매칭 중"),
    CANCEL("취소"),
    ACCEPT("수락"),
    REJECT("거절"),
    DENIED("거절당함"),
    FOUND_BUDDY("상대 찾음"),
    MATCHING_COMPLETED("매칭 성사"),
    REACTIVATE("재신청 가능");
    private final String value;
}
