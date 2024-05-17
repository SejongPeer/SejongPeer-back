package com.sejong.sejongpeer.domain.lecturestudy.repository;

import com.sejong.sejongpeer.domain.lecturestudy.entity.LectureStudy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureStudyRepository extends JpaRepository<LectureStudy, Long> {

	Optional<LectureStudy> findByStudyId(Long studyId);
}
