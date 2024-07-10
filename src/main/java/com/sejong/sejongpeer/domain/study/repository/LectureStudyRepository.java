package com.sejong.sejongpeer.domain.study.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.study.entity.LectureStudy;
import com.sejong.sejongpeer.domain.study.entity.Study;

public interface LectureStudyRepository extends JpaRepository<LectureStudy, Long> {

	Optional<LectureStudy> findByStudy(Study study);
}
