package com.sejong.sejongpeer.domain.honbab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;

public interface HonbabRepository extends JpaRepository<Honbab, Long> {
	List<Honbab> findAllByStatus(HonbabStatus status);

	@Query("SELECT b FROM Honbab b WHERE b.member.id = :memberId ORDER BY b.id DESC LIMIT 1")
	Optional<Honbab> findLastHonbabByMemberId(@Param("memberId") String memberId);

	@Query("SELECT COUNT(b) FROM Honbab b WHERE b.status = 'IN_PROGRESS'")
	Long countByStatusInProgressHonbab();

	Long countByStatus(HonbabStatus inProgress);
}
