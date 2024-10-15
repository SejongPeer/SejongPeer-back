package com.sejong.sejongpeer.domain.member.service;

import java.util.function.Predicate;

import com.sejong.sejongpeer.domain.member.dto.request.MemberUpdateRequest;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.Member;
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

	public void verifyUpdatable(Member member, MemberUpdateRequest request) {
		// 원자성을 보장하기 위해, 중복된 정보가 있으면 업데이트가 중단됨
		validateFieldChange(member.getNickname(), request.nickname(), this::existsNickname, ErrorCode.DUPLICATED_NICKNAME);
		validateFieldChange(member.getPhoneNumber(), request.phoneNumber(), this::existsPhoneNumber, ErrorCode.DUPLICATED_PHONE_NUMBER);
		validateFieldChange(member.getKakaoAccount(), request.kakaoAccount(), this::existsKakaoAccount, ErrorCode.DUPLICATED_KAKAO_ACCOUNT);
	}

	private void validateFieldChange(String currentValue, String newValue, Predicate<String> existsPredicate, ErrorCode errorCode) {
		// 값이 변경된 경우만 중복 검사
		if (newValue != null && !newValue.equals(currentValue) && existsPredicate.test(newValue)) {
			throw new CustomException(errorCode);
		}
	}
}
