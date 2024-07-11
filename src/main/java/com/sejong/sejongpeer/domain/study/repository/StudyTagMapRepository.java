package com.sejong.sejongpeer.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.study.entity.StudyTagMap;

public interface StudyTagMapRepository extends JpaRepository<StudyTagMap, Long> {
}
