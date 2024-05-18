package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.externalactivitystudy.entity.ExternalActivityStudy;
import com.sejong.sejongpeer.domain.externalactivitystudy.repository.ExternalActivityStudyRepository;
import com.sejong.sejongpeer.domain.lecturestudy.entity.LectureStudy;
import com.sejong.sejongpeer.domain.lecturestudy.repository.LectureStudyRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyFindResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyTotalPostResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyUpdateResponse;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
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
public class StudyService {

	private static final String UNIVERSITY_LECTURE_STUDY = "학교수업스터디";
	private static final String EXTERNAL_ACTIVITY_STUDY = "수업외활동";

	private final LectureStudyRepository lectureStudyRepository;
	private final ExternalActivityStudyRepository externalActivityStudyRepository;
	private final StudyRepository studyRepository;
	private final MemberRepository memberRepository;

	public StudyCreateResponse createStudy(final String memberId, final StudyCreateRequest studyCreateRequest) {
		final Member member = memberRepository
			.findById(memberId)
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
		Study study = createStudyEntity(member, studyCreateRequest);
		Study saveStudy = studyRepository.save(study);
		return StudyCreateResponse.from(saveStudy);
	}

	private Study createStudyEntity(final Member member, final StudyCreateRequest studyCreateRequest) {
		return Study.createStudy(
			studyCreateRequest.title(),
			studyCreateRequest.content(),
			studyCreateRequest.recruitmentCount(),
			studyCreateRequest.type(),
			studyCreateRequest.recruitmentStartAt(),
			studyCreateRequest.recruitmentEndAt(),
			member);
	}

	@Transactional(readOnly = true)
	public StudyFindResponse findOneStudy(final Long studyId) {
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

	public StudyUpdateResponse updateStudy(final StudyUpdateRequest studyUpdateRequest, final Long studyId) {
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

	public void deleteStudy(final Long studyId) {
		studyRepository.deleteById(studyId);
	}

	@Transactional(readOnly = true)
	public Slice<StudyTotalPostResponse> getAllStudyPost(String choice, int page, int size) {
		LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<Study> studySlice;

		if (UNIVERSITY_LECTURE_STUDY.equals(choice)) {
			studySlice = studyRepository.findByTypeAndCreatedAtAfter(StudyType.LECTURE, sixMonthsAgo, pageable);
			return mapToStudyTotalPostResponse(studySlice, StudyType.LECTURE);
		}

		if (EXTERNAL_ACTIVITY_STUDY.equals(choice)) {
			studySlice = studyRepository.findByTypeAndCreatedAtAfter(StudyType.EXTERNAL_ACTIVITY, sixMonthsAgo, pageable);
			return mapToStudyTotalPostResponse(studySlice, StudyType.EXTERNAL_ACTIVITY);
		}

		return new SliceImpl<>(Collections.emptyList(), pageable, false);
	}

	private Slice<StudyTotalPostResponse> mapToStudyTotalPostResponse(Slice<Study> studySlice, StudyType studyType) {
		return studySlice.map(study -> {
			if (studyType == StudyType.LECTURE) {
				LectureStudy lectureStudy = lectureStudyRepository.findByStudyId(study.getId())
					.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_AND_STUDY_NOT_CONNECTED));
				String lectureName = lectureStudy.getLecture().getName();
				return StudyTotalPostResponse.fromLectureStudy(study, lectureName);
			} else if (studyType == StudyType.EXTERNAL_ACTIVITY) {
				ExternalActivityStudy externalActivityStudy = externalActivityStudyRepository.findByStudyId(study.getId())
					.orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_AND_STUDY_NOT_CONNECTED));
				String activityCategoryName = externalActivityStudy.getExternalActivity().getName();
				return StudyTotalPostResponse.fromExternalActivityStudy(study, activityCategoryName);
			} else {
				throw new CustomException(ErrorCode.STUDY_TYPE_NOT_FOUND);
			}
		});
	}
}
