package com.sejong.sejongpeer.domain.study.dto.response;

import com.sejong.sejongpeer.domain.study.entity.Study;
import io.swagger.v3.oas.annotations.media.Schema;

public record StudyUpdateResponse(
        @Schema(description = "스터디 ID", defaultValue = "1") Long studyId) {
    public static StudyUpdateResponse from(Study study) {
        return new StudyUpdateResponse(study.getId());
    }
}
