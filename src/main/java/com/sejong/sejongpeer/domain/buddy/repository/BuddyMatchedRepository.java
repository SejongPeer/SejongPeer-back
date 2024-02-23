package com.sejong.sejongpeer.domain.buddy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;

public interface BuddyMatchedRepository extends JpaRepository<BuddyMatched, Long> {
}
