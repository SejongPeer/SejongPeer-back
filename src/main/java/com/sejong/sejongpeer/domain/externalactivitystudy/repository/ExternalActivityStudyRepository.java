package com.sejong.sejongpeer.domain.externalactivitystudy.repository;

import com.sejong.sejongpeer.domain.externalactivitystudy.entity.ExternalActivityStudy;
import com.sejong.sejongpeer.domain.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalActivityStudyRepository extends JpaRepository<ExternalActivityStudy, Long> {

	Optional<ExternalActivityStudy> findByStudy(Study study);
}
