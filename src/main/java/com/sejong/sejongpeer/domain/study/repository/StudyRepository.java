package com.sejong.sejongpeer.domain.study.repository;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;

public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom {

	Slice<Study> findByTypeAndCreatedAtBetween(StudyType studyType, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	Long countByTypeAndCreatedAtBetween(StudyType studyType, LocalDateTime startDate, LocalDateTime endDate);
}
