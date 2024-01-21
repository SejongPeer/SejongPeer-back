package com.sejong.sejongpeer.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	Optional<Member> findByAccount(String account);
}
