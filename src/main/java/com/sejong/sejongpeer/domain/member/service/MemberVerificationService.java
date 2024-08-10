package com.sejong.sejongpeer.domain.member.service;

import com.sejong.sejongpeer.domain.member.dto.request.MemberUpdateRequest;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class MemberVerificationService {

	private final MemberRepository memberRepository;

	public void verifySignUp(SignUpRequest request) {
		verifyPassword(request.password(), request.passwordCheck());
		verifyPhoneNumber(request.phoneNumber());
		verifyAccount(request.account());
		verifyStudentId(request.studentId());
		verifyNickname(request.nickname());
		verifyKakaoAccount(request.kakaoAccount());
	}

	private void verifyPassword(String password, String passwordCheck) {
		if (!password.equals(passwordCheck)) {
			throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
		}
	}

	private void verifyPhoneNumber(String phoneNumber) {
		if (existsPhoneNumber(phoneNumber)) {
			throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
		}
	}

	public boolean existsPhoneNumber(String phoneNumber) {
		return memberRepository.existsByPhoneNumber(phoneNumber);
	}

	private void verifyAccount(String account) {
		if (existsAccount(account)) {
			throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
		}
	}

	private boolean existsAccount(String account) {
		return memberRepository.existsByAccount(account);
	}

	private void verifyStudentId(String studentId) {
		if (existsStudentId(studentId)) {
			throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
		}
	}

	private boolean existsStudentId(String studentId) {
		return memberRepository.existsByStudentId(studentId);
	}

	private void verifyNickname(String nickname) {
		if (existsNickname(nickname)) {
			throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
		}
	}

	public boolean existsNickname(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	private void verifyKakaoAccount(String kakaoAccount) {
		if (existsKakaoAccount(kakaoAccount)) {
			throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ACCOUNT);
		}
	}

	public boolean existsKakaoAccount(String kakaoAccount) {
		return memberRepository.existsByKakaoAccount(kakaoAccount);
	}

	public void verifyUpdatable(MemberUpdateRequest request) {
		// 원자성 보장을 위해 하나라도 잘못되거나 중복된 정보가 있으면 업데이트 되어서는 안됨
		if (request.nickname() != null && existsNickname(request.nickname())) {
			throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
		}

		if (request.phoneNumber() != null && existsPhoneNumber(request.phoneNumber())) {
			throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
		}

		if (request.kakaoAccount() != null && existsKakaoAccount(request.kakaoAccount())) {
			throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ACCOUNT);
		}
	}
}
