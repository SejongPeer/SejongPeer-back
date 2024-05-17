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

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
	public List<StudyTotalPostResponse> getAllLectureStudyPost(String choice) {

		List<Study> studyList = new ArrayList<>();

		if ("학교수업스터디".equals(choice)) {
			studyList = studyRepository.findByType(StudyType.LECTURE);
			for (Study st : studyList) {
				System.out.println(st.getContent());
			}

			return studyList.stream()
				.map(this::mapToLectureStudyTotalPostResponse)
				.collect(Collectors.toList());
		}

		if ("수업외활동".equals(choice)) {
			studyList = studyRepository.findByType(StudyType.EXTERNAL_ACTIVITY);
			for (Study st : studyList) {
				System.out.println(st.getContent());
			}

			return studyList.stream()
				.map(this::mapToExternalActivityStudyTotalPostResponse)
				.collect(Collectors.toList());
		}

		return Collections.emptyList();
	}

	private StudyTotalPostResponse mapToLectureStudyTotalPostResponse(Study study) {
		boolean hasImage = true;
		if (study.getImageUrl() == null) hasImage = false;

		LectureStudy lectureStudy = lectureStudyRepository.findByStudyId(study.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_AND_STUDY_NOT_CONNECTED));

		String lectureName = lectureStudy.getLecture().getName();

		return new StudyTotalPostResponse(
			study.getId(),
			study.getTitle(),
			study.getCreatedAt().toString().substring(0, 10),
			hasImage,
			lectureName
		);
	}

	private StudyTotalPostResponse mapToExternalActivityStudyTotalPostResponse(Study study) {
		boolean hasImage = true;
		if (study.getImageUrl() == null) hasImage = false;

		ExternalActivityStudy externalActivityStudy = externalActivityStudyRepository.findByStudyId(study.getId())
			.orElseThrow(() -> new CustomException(ErrorCode.ACTIVITY_AND_STUDY_NOT_CONNECTED));

		String activityCategoryName = externalActivityStudy.getExternalActivity().getName();

		return new StudyTotalPostResponse(
			study.getId(),
			study.getTitle(),
			study.getCreatedAt().toString().substring(0, 10),
			hasImage,
			activityCategoryName
		);
	}
}
