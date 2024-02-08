package com.sejong.sejongpeer.domain.member.repository;

import com.sejong.sejongpeer.TestQuerydslConfig;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.entity.type.Status;
import com.sejong.sejongpeer.global.config.JpaAuditingConfig;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaAuditingConfig.class, TestQuerydslConfig.class})
class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;

    @Test
    void 회원가입_유저를_저장한다() {
        // given
        Member member =
                Member.builder()
                        .phoneNumber("01012345678")
                        .grade(1)
                        .gender(Gender.MALE)
                        .studentId("12345678")
                        .name("홍길동")
                        .email("test@test.ac.kr")
                        .birthday(LocalDate.of(1999, 1, 1))
                        .major("computer science")
                        .account("test")
                        .password("test")
                        .build();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        Assertions.assertNotNull(savedMember);
        Assertions.assertNotNull(savedMember.getId());
        Assertions.assertEquals(member.getStatus(), Status.ACTIVE);
    }
}
