package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.study.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudyCreateResponse(@Schema(description = "스터디 ID") Long studyId) {
    public static StudyCreateResponse from(Study study) {
        return new StudyCreateResponse(study.getId());
    }
}
