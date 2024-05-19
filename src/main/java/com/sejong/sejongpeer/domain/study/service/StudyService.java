package com.sejong.sejongpeer.domain.study.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {
	private final StudyRepository studyRepository;
	private final SecurityUtil securityUtil;

	public void updateStudy(StudyUpdateRequest request, Long studyId) {
		Study study =
			studyRepository
				.findById(studyId)
				.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		validateOwner(study);

		study.update(request);
		studyRepository.save(study);
	}

	private void validateOwner(Study study) {
		boolean isOwner = study.getMember().getId().equals(securityUtil.getCurrentMemberId());

		if (!isOwner) {
			throw new CustomException(ErrorCode.STUDY_NOT_OWNER);
		}
	}
}
