package com.sejong.sejongpeer.domain.honbab.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.HonbabMatched;

public interface HonbabMatchedRepository extends JpaRepository<HonbabMatched, Long> {
	@Query("SELECT bm FROM HonbabMatched bm WHERE bm.owner = :owner OR bm.partner = :owner ORDER BY bm.id DESC LIMIT 1")
	Optional<HonbabMatched> findLatestByOwnerOrPartner(@Param("owner") Honbab owner);

}
