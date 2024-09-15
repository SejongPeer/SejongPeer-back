package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.dto.response.StudyPostInfoNoLoginResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyTotalPostNoLoginResponse;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyNoLoginService {

	private final StudyRepository studyRepository;

	private final StudyService studyService;

	@Transactional(readOnly = true)
	public Slice<StudyTotalPostNoLoginResponse> getAllStudyPostWithoutLogin(StudyType studyType, int page) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDate = now.minusMonths(page * 6);
		LocalDateTime startDate = endDate.minusMonths(6);

		Pageable pageable;
		Slice<Study> studySlice;

		if (StudyType.LECTURE.equals(studyType)) {
			int size = studyRepository.countByTypeAndCreatedAtBetween(StudyType.LECTURE, startDate, endDate).intValue();
			pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
			studySlice = studyRepository.findByTypeAndCreatedAtBetween(StudyType.LECTURE, startDate, endDate, pageable);
			return mapToStudyTotalPostResponse(studySlice, StudyType.LECTURE);
		}

		if (StudyType.EXTERNAL_ACTIVITY.equals(studyType)) {
			int size = studyRepository.countByTypeAndCreatedAtBetween(StudyType.EXTERNAL_ACTIVITY, startDate, endDate).intValue();
			pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
			studySlice = studyRepository.findByTypeAndCreatedAtBetween(StudyType.EXTERNAL_ACTIVITY, startDate, endDate, pageable);
			return mapToStudyTotalPostResponse(studySlice, StudyType.EXTERNAL_ACTIVITY);
		}

		return new SliceImpl<>(Collections.emptyList(), Pageable.unpaged(), false);
	}

	private Slice<StudyTotalPostNoLoginResponse> mapToStudyTotalPostResponse(Slice<Study> studySlice, StudyType studyType) {
		return studySlice.map(study -> {
			String categoryName = studyService.getCategoryNameByStudyType(study);
			int scrapCount = studyService.getScrapCountByStudy(study);

			if (studyType == StudyType.LECTURE) {
				return StudyTotalPostNoLoginResponse.fromLectureStudy(study, categoryName, scrapCount);
			} else if (studyType == StudyType.EXTERNAL_ACTIVITY) {
				return StudyTotalPostNoLoginResponse.fromExternalActivityStudy(study, categoryName, scrapCount);
			} else {
				throw new CustomException(ErrorCode.STUDY_TYPE_NOT_FOUND);
			}
		});
	}

	@Transactional(readOnly = true)
	public StudyPostInfoNoLoginResponse getOneStudyPostInfoWithoutLogin(final Long studyId) {
		Study study = studyRepository.findById(studyId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		String categoryName = studyService.getCategoryNameByStudyType(study);

		int scrapCount = studyService.getScrapCountByStudy(study);

		return StudyPostInfoNoLoginResponse.fromStudy(study, categoryName, scrapCount);
	}
}
