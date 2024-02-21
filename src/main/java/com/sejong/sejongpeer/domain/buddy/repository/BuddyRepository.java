package com.sejong.sejongpeer.domain.buddy.repository;

import com.sejong.sejongpeer.domain.buddy.entity.Buddy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
}
