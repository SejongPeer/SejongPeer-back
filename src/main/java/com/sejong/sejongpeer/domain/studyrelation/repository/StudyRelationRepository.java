package com.sejong.sejongpeer.domain.studyrelation.repository;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;

import java.util.*;

public interface StudyRelationRepository
	extends JpaRepository<StudyRelation, String>, StudyRelationRepositoryCustom {

	List<StudyRelation> findByMemberAndStudy(Member member, Study study);

	Long countByStudyAndStatus(Study study, StudyMatchingStatus status);
}
