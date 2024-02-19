package com.sejong.sejongpeer.domain.StudyRelations.repository;

import com.sejong.sejongpeer.domain.StudyRelations.domain.StudyRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRelationRepository
        extends JpaRepository<StudyRelation, String>, StudyRelationRepositoryCustom {}
