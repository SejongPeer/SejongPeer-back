package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyPostSearchRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.*;
import com.sejong.sejongpeer.domain.study.entity.ExternalActivityStudy;
import com.sejong.sejongpeer.domain.study.entity.LectureStudy;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.domain.study.repository.ExternalActivityStudyRepository;
import com.sejong.sejongpeer.domain.study.repository.LectureStudyRepository;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;
import com.sejong.sejongpeer.domain.studyrelation.repository.StudyRelationRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyService {

	private final LectureStudyRepository lectureStudyRepository;
	private final ExternalActivityStudyRepository externalActivityStudyRepository;
	private final StudyRepository studyRepository;
	private final StudyRelationRepository studyRelationRepository;
	private final MemberUtil memberUtil;

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

	@Transactional(readOnly = true)
	public Slice<StudyTotalPostResponse> getAllStudyPost(StudyType studyType, int page) {
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

	private Slice<StudyTotalPostResponse> mapToStudyTotalPostResponse(Slice<Study> studySlice, StudyType studyType) {
		return studySlice.map(study -> {
			String categoryName = getCategoryNameByStudyType(study);
			if (studyType == StudyType.LECTURE) {
				return StudyTotalPostResponse.fromLectureStudy(study, categoryName);
			} else if (studyType == StudyType.EXTERNAL_ACTIVITY) {
				return StudyTotalPostResponse.fromExternalActivityStudy(study, categoryName);
			} else {
				throw new CustomException(ErrorCode.STUDY_TYPE_NOT_FOUND);
			}
		});
	}

	@Transactional(readOnly = true)
	public StudyPostInfoResponse getOneStudyPostInfo(Long studyId) {
		Study study = studyRepository.findById(studyId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
		String categoryName = getCategoryNameByStudyType(study);

		Long acceptedApplicantsCount = studyRelationRepository.countByStudyAndStatus(study, StudyMatchingStatus.ACCEPT);
		return StudyPostInfoResponse.fromStudy(study, categoryName, acceptedApplicantsCount);
	}

	private String getCategoryNameByStudyType(Study study) {
		if (study.getType() == StudyType.LECTURE) {
			LectureStudy lectureStudy = lectureStudyRepository.findByStudy(study)
				.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_AND_STUDY_NOT_CONNECTED));
			return lectureStudy.getLecture().getName();
		} else if (study.getType() == StudyType.EXTERNAL_ACTIVITY) {
			ExternalActivityStudy externalActivityStudy = externalActivityStudyRepository.findByStudy(study)
				.orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_AND_STUDY_NOT_CONNECTED));
			return externalActivityStudy.getExternalActivity().getName();
		} else {
			throw new CustomException(ErrorCode.STUDY_TYPE_NOT_FOUND);
		}
	}

	@Transactional(readOnly = true)
	public List<StudyTotalPostResponse> getAllStudyPostBySearch(Integer page, Integer size, StudyPostSearchRequest request) {
		if (request.recruitmentMin() > request.recruitmentMax()) {
			throw new CustomException(ErrorCode.STUDY_SEARCH_PERSONNEL_MISCONDITION);
		}

		Specification<Study> spec = Specification.where(StudySpecification.checkBiggerThanRecruitmentMin(request.recruitmentMin()))
			.and(StudySpecification.checkSmallerThanRecruitmentMax(request.recruitmentMax()))
			.and(StudySpecification.checkAfterStartedAt(request.recruitmentStartAt()))
			.and(StudySpecification.checkBeforeClosedAt(request.recruitmentEndAt()))
			.and(StudySpecification.findByRecruitmentStatus(request.isRecruiting()))
			.and(StudySpecification.containsTitleOrContent(request.searchWord()));

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<Study> studyPage = studyRepository.findAll(spec, pageable);

		return studyPage.stream()
			.map(study -> {
				String categoryName = getCategoryNameByStudyType(study);
				if (study.getType() == StudyType.LECTURE) {
					return StudyTotalPostResponse.fromLectureStudy(study, categoryName);
				} else if (study.getType() == StudyType.EXTERNAL_ACTIVITY) {
					return StudyTotalPostResponse.fromExternalActivityStudy(study, categoryName);
				} else {
					throw new CustomException(ErrorCode.STUDY_TYPE_NOT_FOUND);
				}
			})
			.collect(Collectors.toUnmodifiableList());

	}
}
