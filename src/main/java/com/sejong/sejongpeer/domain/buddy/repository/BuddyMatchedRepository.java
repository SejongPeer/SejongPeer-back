package com.sejong.sejongpeer.domain.buddy.repository;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BuddyMatchedRepository extends JpaRepository<BuddyMatched, Long> {

	@Query("SELECT bm FROM BuddyMatched bm WHERE bm.owner = :ownerBuddy OR bm.partner = :ownerBuddy")
	Optional<BuddyMatched> findByOwnerOrPartner(@Param("ownerBuddy") Buddy ownerBuddy);
}
