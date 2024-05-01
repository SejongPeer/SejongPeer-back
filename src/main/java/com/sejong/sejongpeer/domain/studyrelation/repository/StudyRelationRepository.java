package com.sejong.sejongpeer.domain.studyrelation.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;

public interface StudyRelationRepository
	extends JpaRepository<StudyRelation, String>, StudyRelationRepositoryCustom {
}
