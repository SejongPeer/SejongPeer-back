package com.sejong.sejongpeer.domain.StudyRelations.api;

import com.sejong.sejongpeer.domain.StudyRelations.service.StudyRelationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "3. [스터디 신청현황]", description = "스터디 신청 현황 API입니다.")
@RestController
@RequestMapping("/api/v1/study/relations")
@RequiredArgsConstructor
public class StudyRelationController {

    private final StudyRelationService studyRelationService;

    @Operation(summary = "스터디 신청", description = "스터디를 신청합니다.")
    @PostMapping
    public ResponseEntity<?> relationCreate(@RequestParam("studyId") Long studyId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @Operation(summary = "스터디 취소", description = "스터디 신청을 취소합니다.")
    @DeleteMapping
    public ResponseEntity<?> relationDelete() {
        return null;
    }

    @Operation(summary = "스터디 수락", description = "스터디 신청을 취소합니다.")
    @PutMapping("/accept")
    public ResponseEntity<?> relationAccept() {
        return null;
    }

    @Operation(summary = "스터디 거부", description = "스터디 신청을 취소합니다.")
    @PutMapping("/reject")
    public ResponseEntity<?> relationReject() {
        return null;
    }
}
