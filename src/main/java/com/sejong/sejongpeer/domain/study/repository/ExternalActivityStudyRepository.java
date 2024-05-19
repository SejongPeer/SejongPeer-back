package com.sejong.sejongpeer.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.study.entity.ExternalActivityStudy;

public interface ExternalActivityStudyRepository extends JpaRepository<ExternalActivityStudy, Long> {
}
