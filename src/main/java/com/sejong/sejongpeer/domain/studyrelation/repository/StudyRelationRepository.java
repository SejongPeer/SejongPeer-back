package com.sejong.sejongpeer.domain.studyrelation.repository;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;

public interface StudyRelationRepository
	extends JpaRepository<StudyRelation, String>, StudyRelationRepositoryCustom {

	Optional<StudyRelation> findTopByMemberAndStudyOrderByIdDesc(Member member, Study study);

	List<StudyRelation> findByStudyId(Long studyId);
	List<StudyRelation> findByMember(Member member);

	Optional<StudyRelation> findTopByMemberIdAndStudyIdOrderByIdDesc(String memberId, Long studyId);

	List<StudyRelation> findByStudyAndStatusNot(Study study, StudyMatchingStatus status);

	boolean existsByMemberAndStudyAndStatusNot(Member member, Study study, StudyMatchingStatus status);

	List<StudyRelation> findAllByStudyAndStatus(Study study, StudyMatchingStatus status);
}
