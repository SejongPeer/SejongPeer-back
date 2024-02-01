package com.sejong.sejongpeer.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.member.dto.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public void signUp(SignUpRequest request) {
		verifySignUp(request);

		String encodedPassword = passwordEncoder.encode(request.password());
		Member member = Member.create(request, encodedPassword);

		memberRepository.save(member);
	}

	private void verifySignUp(SignUpRequest request) {
		if (!request.password().equals(request.passwordCheck())) {
			throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
		}
		if (isPhoneNumberExists(request.phoneNumber())) {
			throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
		}
		if (isAccountExists(request.account())) {
			throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
		}
		if (isStudentIdExists(request.studentId())) {
			throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
		}
		if (isEmailExists(request.email())) {
			throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
		}
	}

	@Transactional(readOnly = true)
	public boolean isEmailExists(String email) {
		return memberRepository.existsByEmail(email);
	}

	@Transactional(readOnly = true)
	public boolean isStudentIdExists(String studentId) {
		return memberRepository.existsByStudentId(studentId);
	}

	@Transactional(readOnly = true)
	public boolean isAccountExists(String account) {
		return memberRepository.existsByAccount(account);
	}

	@Transactional(readOnly = true)
	public boolean isPhoneNumberExists(String phoneNumber) {
		return memberRepository.existsByPhoneNumber(phoneNumber);
	}
}
