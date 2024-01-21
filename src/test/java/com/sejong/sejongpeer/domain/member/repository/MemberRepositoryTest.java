package com.sejong.sejongpeer.domain.member.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Status;
import com.sejong.sejongpeer.util.DomainObjectUtil;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
	@Autowired
	private MemberRepository memberRepository;

	@Test
	void 회원가입_유저를_저장한다() {
		// given
		Member member = DomainObjectUtil.createInstance(Member.class, null);

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
