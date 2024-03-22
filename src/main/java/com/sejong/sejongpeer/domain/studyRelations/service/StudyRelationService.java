package com.sejong.sejongpeer.domain.studyRelations.service;

import com.sejong.sejongpeer.domain.studyRelations.dto.response.StudyRelationCreateResponse;
import com.sejong.sejongpeer.domain.studyRelations.repository.StudyRelationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRelationService {

	private final StudyRelationRepository studyRelationRepository;

	public StudyRelationCreateResponse createStudyRelation(final Long studyId) {
		return null;
	}
}
