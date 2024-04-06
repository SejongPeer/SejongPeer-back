package com.sejong.sejongpeer.domain.studyrelation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.studyrelation.dto.response.StudyRelationCreateResponse;
import com.sejong.sejongpeer.domain.studyrelation.repository.StudyRelationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRelationService {

	private final StudyRelationRepository studyRelationRepository;

	public StudyRelationCreateResponse createStudyRelation(final Long studyId) {
		return null;
	}
}
