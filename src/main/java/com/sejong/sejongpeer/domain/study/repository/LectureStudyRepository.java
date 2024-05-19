package com.sejong.sejongpeer.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.study.entity.LectureStudy;

public interface LectureStudyRepository extends JpaRepository<LectureStudy, Long> {
}
