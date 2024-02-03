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

    // 회원가입 에러
    SIGN_UP_ERROR(HttpStatus.BAD_REQUEST, "회원가입에 실패하였습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    DUPLICATED_STUDENT_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 학번입니다."),
    DUPLICATED_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 전화번호입니다."),
    DUPLICATED_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 존재하는 계정입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
