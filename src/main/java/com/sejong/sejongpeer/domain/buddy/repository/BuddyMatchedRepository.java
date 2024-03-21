package com.sejong.sejongpeer.domain.buddy.repository;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BuddyMatchedRepository extends JpaRepository<BuddyMatched, Long> {
	@Query("SELECT bm FROM BuddyMatched bm WHERE bm.owner = :owner OR bm.partner = :owner ORDER BY bm.id DESC LIMIT 1")
	Optional<BuddyMatched> findLatestByOwnerOrPartner(@Param("owner") Buddy owner);

	@Query("SELECT bm FROM BuddyMatched bm WHERE " +
		"(:owner = bm.owner OR :owner = bm.partner)")
	Optional<BuddyMatched> findByOwnerOrPartner(@Param("owner") Buddy owner);
}
