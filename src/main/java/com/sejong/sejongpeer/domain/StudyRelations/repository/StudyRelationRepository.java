package com.sejong.sejongpeer.domain.StudyRelations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.StudyRelations.domain.StudyRelation;

public interface StudyRelationRepository extends JpaRepository<StudyRelation, String>, StudyRelationRepositoryCustom {
}
