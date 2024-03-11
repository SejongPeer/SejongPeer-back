package com.sejong.sejongpeer.domain.buddy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.member.entity.Member;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
	List<Buddy> findAllByStatus(BuddyStatus buddyStatus);

	Optional<Buddy> findTopByMemberAndStatusOrderByCreatedAtDesc(Member member, BuddyStatus status);

	@Query("SELECT b FROM Buddy b WHERE b.member.id = :memberId ORDER BY b.id DESC LIMIT 1")
	Optional<Buddy> findLastBuddyByMemberId(@Param("memberId") String memberId);

	@Query("SELECT COUNT(b) FROM Buddy b WHERE b.status = 'IN_PROGRESS' OR b.status = 'FOUND_BUDDY'")
	Long countByStatusInProgressOrFoundBuddy();

	List<Buddy> findAllByMemberIdAndStatus(String memberId, BuddyStatus status);
}
