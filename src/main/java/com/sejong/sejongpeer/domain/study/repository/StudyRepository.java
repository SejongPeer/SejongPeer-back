package com.sejong.sejongpeer.domain.study.repository;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom {

	List<Study> findByType(StudyType studyType);

	Slice<Study> findByTypeAndCreatedAtAfter(StudyType studyType, LocalDateTime createdAt, Pageable pageable);
}
