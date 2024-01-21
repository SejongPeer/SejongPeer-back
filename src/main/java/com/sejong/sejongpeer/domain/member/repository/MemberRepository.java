package com.sejong.sejongpeer.domain.member.repository;

import com.sejong.sejongpeer.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByAccount(String account);
}
