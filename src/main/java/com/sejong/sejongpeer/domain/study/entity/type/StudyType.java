package com.sejong.sejongpeer.domain.study.entity.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudyType {
    PROJECT("프로젝트"),
    CONTEST("공모전"),
    EXTERNAL_ACTIVITY("대외활동"),
    PREPARE_EMPLOYMENT("취업준비"),
    LANGUAGE_STUDY("어학"),
    PROGRAMMING("프로그래밍"),
    ETC("기타");
    private final String value;
}
