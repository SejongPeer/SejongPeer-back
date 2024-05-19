package com.sejong.sejongpeer.domain.studyrelation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;

public interface StudyRelationRepository
	extends JpaRepository<StudyRelation, String>, StudyRelationRepositoryCustom {
	List<StudyRelation> findByStudyId(Long studyId);
}
