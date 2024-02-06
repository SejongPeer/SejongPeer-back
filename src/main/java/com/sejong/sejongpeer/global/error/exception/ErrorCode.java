package com.sejong.sejongpeer.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SAMPLE_ERROR(HttpStatus.BAD_REQUEST, "Sample Error Message"),
    // 서버 에러
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Enum Type이 일치하지 않아 Binding에 실패하였습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP method 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류, 관리자에게 문의하세요"),

	// Study
	STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다.")
	;
    private final HttpStatus status;
    private final String message;
}
