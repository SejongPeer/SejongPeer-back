package com.sejong.sejongpeer.domain.studyrelation.service;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyApplyRequest;
import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.studyrelation.dto.response.StudyRelationCreateResponse;
import com.sejong.sejongpeer.domain.studyrelation.repository.StudyRelationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRelationService {

	private final StudyRepository studyRepository;
	private final StudyRelationRepository studyRelationRepository;
	private final MemberUtil memberUtil;

	public StudyRelationCreateResponse applyStudy(StudyApplyRequest studyApplyRequest) {
		Study study = studyRepository.findById(studyApplyRequest.studyId())
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		StudyRelation studyapplication = StudyRelation.createStudyRelations(memberUtil.getCurrentMember(),study);
		studyRelationRepository.save(studyapplication);

		return StudyRelationCreateResponse.from(studyapplication);
	}
}
