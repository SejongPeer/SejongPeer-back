package com.sejong.sejongpeer.domain.buddy.repository;

import java.util.List;

import com.sejong.sejongpeer.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
	List<Buddy> findByStatus(BuddyStatus buddyStatus);
	List<Buddy> findAllByMemberOrderByCreatedAtDesc(Member member);
}
