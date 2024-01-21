package com.sejong.sejongpeer.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
