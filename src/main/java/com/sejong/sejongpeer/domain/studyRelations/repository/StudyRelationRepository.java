package com.sejong.sejongpeer.domain.studyRelations.repository;

import com.sejong.sejongpeer.domain.studyRelations.domain.StudyRelation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRelationRepository
	extends JpaRepository<StudyRelation, String>, StudyRelationRepositoryCustom {
}
