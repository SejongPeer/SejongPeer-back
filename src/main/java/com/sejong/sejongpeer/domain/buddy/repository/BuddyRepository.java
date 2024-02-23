package com.sejong.sejongpeer.domain.buddy.repository;

import com.sejong.sejongpeer.domain.buddy.entity.Buddy;
import com.sejong.sejongpeer.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
	List<Buddy> findAllByMemberOrderByCreatedAtDesc(Member member);
}
