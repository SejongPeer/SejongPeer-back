package com.sejong.sejongpeer.domain.honbab.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;

public interface HonbabRepository extends JpaRepository<Honbab, Long> {
	List<Honbab> findAllByStatus(HonbabStatus status);
}
