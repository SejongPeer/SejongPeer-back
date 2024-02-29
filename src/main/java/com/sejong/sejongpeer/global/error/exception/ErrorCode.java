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

    // 전공 에러
    COLLEGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 대학입니다."),

    // 인증 에러
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "세션이 만료되었습니다."),

    // 회원가입 및 회원정보 수정 에러
    SIGN_UP_ERROR(HttpStatus.BAD_REQUEST, "회원가입에 실패하였습니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    DUPLICATED_STUDENT_ID(HttpStatus.CONFLICT, "이미 존재하는 학번입니다."),
    DUPLICATED_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 전화번호입니다."),
    DUPLICATED_ACCOUNT(HttpStatus.CONFLICT, "이미 존재하는 계정입니다."),
    DUPLICATED_KAKAO_ACCOUNT(HttpStatus.BAD_REQUEST, "이미 사용중인 카카오 계정입니다."),

    // 조회 에러
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),

    // ID/PW 찾기 에러
    ACCOUNT_FIND_ERROR(HttpStatus.BAD_REQUEST, "이름 또는 전화번호를 입력해주세요."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 정보로 가입된 계정이 존재하지 않습니다."),
    ACCOUNT_FIND_OPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 인증 방법입니다."),

    // 버디 에러
    BUDDY_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 버디가 없습니다"),
    NOT_IN_PROGRESS(HttpStatus.CONFLICT, "버디 매칭중이 아닙니다."),
    REGISTRATION_NOT_POSSIBLE(HttpStatus.CONFLICT, "버디 신청을 할 수 없습니다."),
    TARGET_BUDDY_NOT_FOUND(HttpStatus.NOT_FOUND, "상대 버디를 찾을 수 없습니다."),
    BUDDY_NOT_MATCHED(HttpStatus.CONFLICT, "매칭 성사된 버디가 아닙니다."),

    // Study
    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."),

    // 혼밥 에러
    HONBAB_NOT_FOUND(HttpStatus.NOT_FOUND, "신청한 혼밥이 없습니다"),


	// 버디 등록 거절
	REJECT_PENALTY(HttpStatus.FORBIDDEN, "짝매칭 이후 거절한 유저는 1시간 동안 버디를 등록할 수 없습니다."),

	// 혼밥 에러
	HONBAB_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 혼밥 짝꿍이 없습니다"),
	TARGET_HONBAB_NOT_FOUND(HttpStatus.NOT_FOUND, "혼밥 짝꿍을 찾을 수 없습니다.");
    private final HttpStatus status;
    private final String message;

}

