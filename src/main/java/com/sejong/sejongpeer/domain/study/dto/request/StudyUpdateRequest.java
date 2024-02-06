package com.sejong.sejongpeer.domain.study.dto.request;

import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import java.time.LocalDateTime;

public record StudyUpdateRequest(
        String title,
        String content,
        StudyType type,
        Integer recruitmentCount,
        LocalDateTime recruitmentStartAt,
        LocalDateTime recruitmentEndAt) {}
