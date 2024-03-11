package com.sejong.sejongpeer.domain.buddy.repository;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BuddyMatchedRepository extends JpaRepository<BuddyMatched, Long> {
	@Query("SELECT bm FROM BuddyMatched bm WHERE bm.owner = :owner OR bm.partner = :owner ORDER BY bm.id DESC LIMIT 1")
	Optional<BuddyMatched> findLatestByOwnerOrPartner(@Param("owner") Buddy owner);

	@Query("SELECT bm FROM BuddyMatched bm WHERE " +
        "((bm.owner = :owner AND bm.partner = :partner) OR (bm.partner = :owner AND bm.owner = :partner)) " +
        "AND bm.owner IS NOT NULL AND bm.partner IS NOT NULL")
	Optional<BuddyMatched> findByOwnerAndPartner(@Param("owner") Buddy owner, @Param("partner") Buddy partner);

}
