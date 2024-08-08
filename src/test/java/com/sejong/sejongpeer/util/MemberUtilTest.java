package com.sejong.sejongpeer.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.util.MemberUtil;
import com.sejong.sejongpeer.security.MemberDetails;

@SpringBootTest
@ActiveProfiles("test")
public class MemberUtilTest {

	@Autowired
	private MemberUtil memberUtil;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	@Autowired
	private CollegeMajorRepository collegeMajorRepository;
	private static final String MEMBER_PASSWORD_TEST = "test";

	private Member member;

	@BeforeEach
	void setUp() {
		SignUpRequest signUpRequest =
			new SignUpRequest(
				"USER",
				"1234",
				"1234",
				"test",
				"18011111",
				"소프트웨어융합대학",
				"컴퓨터공학과 ",
				"소프트웨어융합대학",
				"데이터사이언스학과",
				2,
				Gender.MALE,
				"01011112222",
				"tester",
				"testkakao");
		CollegeMajor major = collegeMajorRepository.save(CollegeMajor.builder()
			.college(signUpRequest.college())
			.major(signUpRequest.major())
			.build());

		member = memberRepository.save(Member.create(
			signUpRequest, major, null, passwordEncoder.encode(MEMBER_PASSWORD_TEST)));

		MemberDetails principal = new MemberDetails(member.getId(), "USER", "1234");
		Authentication authentication =
			new UsernamePasswordAuthenticationToken(
				principal, "1234", principal.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	// @Test
	// void 현재_로그인한_회원의_정보를_정상적으로_반환한다() {
	// 	// when
	// 	Member currentMember = memberUtil.getCurrentMember();
	//
	// 	// then
	// 	assertEquals("test", currentMember.getName());
	// }
}
