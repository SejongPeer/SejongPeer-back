package com.sejong.sejongpeer.domain.member.repository;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sejong.sejongpeer.config.PasswordEncoderTestConfig;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.entity.type.Status;
import com.sejong.sejongpeer.global.config.JpaAuditingConfig;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaAuditingConfig.class, PasswordEncoderTestConfig.class})
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void 회원가입_유저를_저장한다() {
		String encryptedPassword = passwordEncoder.encode("test");
		// given
		Member member =
			Member.builder()
				.phoneNumber("01012341234")
				.grade(1)
				.gender(Gender.MALE)
				.studentId("12345678")
				.name("홍길동")
				.email("test@test.ac.kr")
				.birthday(LocalDate.of(1999, 1, 1))
				.major("computer science")
				.account("test")
				.password(encryptedPassword)
				.build();

		// when
		Member savedMember = memberRepository.save(member);

		// then
		Assertions.assertNotNull(savedMember);
		Assertions.assertNotNull(savedMember.getId());
		Assertions.assertNotNull(savedMember.getCreatedAt());
		Assertions.assertNotNull(savedMember.getUpdatedAt());
		Assertions.assertEquals(member.getStatus(), Status.ACTIVE);
	}
}
