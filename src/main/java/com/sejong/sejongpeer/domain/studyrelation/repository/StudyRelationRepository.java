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

	List<StudyRelation> findByMemberAndStudy(Member member, Study study);

	List<StudyRelation> findByStudyId(Long studyId);
	List<StudyRelation> findByMember(Member member);

	Optional<StudyRelation> findByMemberIdAndStudyId(String memberId, Long studyId);
}
