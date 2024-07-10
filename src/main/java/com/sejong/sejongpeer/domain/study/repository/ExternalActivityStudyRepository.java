package com.sejong.sejongpeer.domain.study.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.study.entity.ExternalActivityStudy;
import com.sejong.sejongpeer.domain.study.entity.Study;

public interface ExternalActivityStudyRepository extends JpaRepository<ExternalActivityStudy, Long> {

	Optional<ExternalActivityStudy> findByStudy(Study study);
}
