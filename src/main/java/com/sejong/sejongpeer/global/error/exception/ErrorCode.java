package com.sejong.sejongpeer.global.error.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "계정과 일치하지 않는 비밀번호입니다."),
	PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
	DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
	DUPLICATED_STUDENT_ID(HttpStatus.CONFLICT, "이미 존재하는 학번입니다."),
	DUPLICATED_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 전화번호입니다."),
	DUPLICATED_ACCOUNT(HttpStatus.CONFLICT, "이미 존재하는 계정입니다."),
	DUPLICATED_KAKAO_ACCOUNT(HttpStatus.CONFLICT, "이미 사용중인 카카오 계정입니다."),

	// 조회 에러
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	NICKNAME_IS_NULL(HttpStatus.BAD_REQUEST, "닉네임이 존재하지 않습니다."),
	PHONE_NUMBER_IS_NULL(HttpStatus.BAD_REQUEST, "전화번호가 존재하지 않습니다."),
	KAKAO_ACCOUNT_IS_NULL(HttpStatus.BAD_REQUEST, "카카오 ID가 존재하지 않습니다."),

	// ID/PW 찾기 에러
	ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 정보로 가입된 계정이 존재하지 않습니다."),

	// 버디 에러
	BUDDY_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 버디가 없습니다"),
	NOT_IN_PROGRESS(HttpStatus.CONFLICT, "매칭중이 아닙니다."),
	REGISTRATION_NOT_POSSIBLE(HttpStatus.CONFLICT, "이미 최대횟수만큼 버디 찾기를 신청했습니다."),
	TARGET_BUDDY_NOT_FOUND(HttpStatus.NOT_FOUND, "상대 버디를 찾을 수 없습니다."),
	BUDDY_NOT_MATCHED(HttpStatus.CONFLICT, "매칭 성사된 버디가 아닙니다."),
	MAX_BUDDY_REGISTRATION_EXCEEDED(HttpStatus.FORBIDDEN, "최대 등록 횟수를 초과했습니다."),

	// Study
	STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "스터디를 찾을 수 없습니다."),
	LECTURE_AND_STUDY_NOT_CONNECTED(HttpStatus.NOT_FOUND, "스터디 게시글에 대응되는 교내 수업이 없습니다."),
	ACTIVITY_AND_STUDY_NOT_CONNECTED(HttpStatus.NOT_FOUND, "스터디 게시글에 대응되는 외부 활동 카테고리가 없습니다."),

	// 버디 등록 거절
	REJECT_PENALTY(HttpStatus.FORBIDDEN, "짝매칭 이후 거절한 유저는 일정시간 동안 버디를 등록할 수 없습니다."),

	// 혼밥 에러
	HONBAB_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 혼밥이 없습니다"),
	TARGET_HONBAB_NOT_FOUND(HttpStatus.NOT_FOUND, "혼밥 짝꿍을 찾을 수 없습니다."),
	HONBAB_REGISTRATION_LIMIT(HttpStatus.CONFLICT, "혼밥 매칭 후 일정시간 동안 재신청을 할 수 없습니다."),

	// 이미지 에러
	IMAGE_FILE_EXTENSION_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지 파일 형식을 찾을 수 없습니다."),
	IMAGE_KEY_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지 키를 찾을 수 없습니다."),
	STUDY_USER_MISMATCH(HttpStatus.FORBIDDEN, "스터디를 생성한 유저와 로그인한 계정이 일치하지 않습니다."),

	STUDY_UPLOAD_STATUS_IS_NOT_NONE(HttpStatus.BAD_REQUEST, "스터디 이미지 업로드 상태가 NONE이 아닙니다."),
	STUDY_UPLOAD_STATUS_IS_NOT_PENDING(HttpStatus.BAD_REQUEST, "스터디 이미지 업로드 상태가 PENDING이 아닙니다."),

	;

	private final HttpStatus status;
	private final String message;

}

