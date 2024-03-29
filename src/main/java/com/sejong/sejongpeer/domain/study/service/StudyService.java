package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyFindResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyUpdateResponse;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

    private final StudyRepository studyRepository;

    public StudyCreateResponse createStudy(final StudyCreateRequest studyCreateRequest) {
        Study study = createStudyEntity(studyCreateRequest);
        Study saveStudy = studyRepository.save(study);
        return StudyCreateResponse.from(saveStudy);
    }

    private Study createStudyEntity(final StudyCreateRequest studyCreateRequest) {
        return Study.createStudy(
                studyCreateRequest.title(),
                studyCreateRequest.content(),
                studyCreateRequest.recruitmentCount(),
                studyCreateRequest.type(),
                studyCreateRequest.recruitmentStartAt(),
                studyCreateRequest.recruitmentEndAt(),
                Member.builder().build());
    }

    @Transactional(readOnly = true)
    public StudyFindResponse findOneStudy(Long studyId) {
        Study study =
                studyRepository
                        .findById(studyId)
                        .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        return StudyFindResponse.from(study);
    }

    @Transactional(readOnly = true)
    public Slice<StudyFindResponse> findSliceStudy(int size, Long lastId) {
        Slice<Study> studySlice = studyRepository.findStudySlice(size, lastId);
        return studySlice.map(StudyFindResponse::from);
    }

    public StudyUpdateResponse updateStudy(StudyUpdateRequest studyUpdateRequest, Long studyId) {
        Study study =
                studyRepository
                        .findById(studyId)
                        .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
        study.updateStudy(
                studyUpdateRequest.title(),
                studyUpdateRequest.content(),
                studyUpdateRequest.recruitmentCount(),
                studyUpdateRequest.type(),
                studyUpdateRequest.recruitmentStartAt(),
                studyUpdateRequest.recruitmentEndAt());
        return StudyUpdateResponse.from(study);
    }

    public void deleteStudy(Long studyId) {
        studyRepository.deleteById(studyId);
    }
}
