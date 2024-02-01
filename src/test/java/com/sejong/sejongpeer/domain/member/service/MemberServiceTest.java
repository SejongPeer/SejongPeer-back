package com.sejong.sejongpeer.domain.member.service;

import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sejong.sejongpeer.config.PasswordEncoderTestConfig;
import com.sejong.sejongpeer.domain.member.dto.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
@Import(PasswordEncoderTestConfig.class)
class MemberServiceTest {
	@InjectMocks
	private MemberService memberService;
	@Mock
	private PasswordEncoder passwordEncoder = new PasswordEncoderTestConfig().passwordEncoder();
	@Mock
	private MemberRepository memberRepository;

	private SignUpRequest signUpRequest;
	private String encryptedPassword;

	@BeforeEach
	void setUp() {
		this.encryptedPassword = passwordEncoder.encode("test");
		this.signUpRequest = new SignUpRequest("test", "test", "test", "test@test.com", "test", "test", "test", 1,
			Gender.MALE, LocalDate.of(1999, 1, 1), "01011112222");
	}

	@Test
	void 정상_회원가입() {
		// given
		Member member = Member.create(signUpRequest, encryptedPassword);
		given(memberRepository.save(any(Member.class))).willReturn(member);
		given(memberRepository.findByAccount("test")).willReturn(Optional.ofNullable(member));

		// when
		Assertions.assertDoesNotThrow(() -> memberService.signUp(signUpRequest));
		Member savedMember = memberRepository.findByAccount("test").get();

		// then
		Assertions.assertEquals(member, savedMember);
	}

	@Test
	void 중복된_아이디_회원가입() {
		// given
		given(memberRepository.existsByAccount("test")).willReturn(true);

		// when
		CustomException e = Assertions.assertThrows(CustomException.class, () -> memberService.signUp(signUpRequest));

		// then
		Assertions.assertEquals(ErrorCode.DUPLICATED_ACCOUNT, e.getErrorCode());
	}

	@Test
	void 중복된_이메일_회원가입() {
		// given
		given(memberRepository.existsByEmail("test@test.com")).willReturn(true);

		// when
		CustomException e = Assertions.assertThrows(CustomException.class, () -> memberService.signUp(signUpRequest));

		// then
		Assertions.assertEquals(ErrorCode.DUPLICATED_EMAIL, e.getErrorCode());
	}

	@Test
	void 중복된_학번_회원가입() {
		// given
		given(memberRepository.existsByStudentId("test")).willReturn(true);

		// when
		CustomException e = Assertions.assertThrows(CustomException.class, () -> memberService.signUp(signUpRequest));

		// then
		Assertions.assertEquals(ErrorCode.DUPLICATED_STUDENT_ID, e.getErrorCode());
	}

	@Test
	void 중복된_전화번호_회원가입() {
		// given
		given(memberRepository.existsByPhoneNumber("01011112222")).willReturn(true);

		// when
		CustomException e = Assertions.assertThrows(CustomException.class, () -> memberService.signUp(signUpRequest));

		// then
		Assertions.assertEquals(ErrorCode.DUPLICATED_PHONE_NUMBER, e.getErrorCode());
	}

	@Test
	void 잘못된_비밀번호_재입력() {
		// given
		this.signUpRequest = new SignUpRequest("test", "password", "anotherPassword", "test@test.com", "test", "test",
			"test", 1,
			Gender.MALE, LocalDate.of(1999, 1, 1), "01011112222");

		// when
		CustomException e = Assertions.assertThrows(CustomException.class, () -> memberService.signUp(signUpRequest));

		// then
		Assertions.assertEquals(ErrorCode.PASSWORD_NOT_MATCH, e.getErrorCode());

	}
}
