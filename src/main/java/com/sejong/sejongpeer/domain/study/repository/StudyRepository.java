package com.sejong.sejongpeer.domain.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.study.entity.Study;

public interface StudyRepository extends JpaRepository<Study, Long>, StudyRepositoryCustom{
}
