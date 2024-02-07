package com.sejong.sejongpeer.domain.member.repository;

import com.sejong.sejongpeer.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByAccount(String account);

    boolean existsByStudentId(String studentId);

    boolean existsByAccount(String account);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByNickname(String nickname);

    Optional<Member> findByPhoneNumberAndStudentId(String phoneNumber, String studentId);

    Optional<Member> findByNameAndStudentId(String name, String studentId);

    Optional<Member> findByAccountAndStudentId(String account, String studentId);
}
