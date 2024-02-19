package com.sejong.sejongpeer.domain.StudyRelations.service;

import com.sejong.sejongpeer.domain.StudyRelations.dto.response.StudyRelationCreateResponse;
import com.sejong.sejongpeer.domain.StudyRelations.repository.StudyRelationRepository;
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
