package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.externalactivity.entity.ExternalActivity;
import com.sejong.sejongpeer.domain.externalactivity.repository.ExternalActivityRepository;
import com.sejong.sejongpeer.domain.lecture.entity.Lecture;
import com.sejong.sejongpeer.domain.lecture.repository.LectureRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.scrap.dao.ScrapRepository;
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
import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;
import com.sejong.sejongpeer.domain.studyrelation.repository.StudyRelationRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;

import com.sejong.sejongpeer.infra.sms.service.SmsService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;
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
	private static final String MESSAGE_ALARM_SEJONGPEER_PREFIX = "[세종피어] ";
	private static final String MESSAGE_ALARM_PARENTHESES_PREFIX = "(";
	private static final String MESSAGE_ALARM_PARENTHESES_POSTFIX = "...) ";

	private final LectureRepository lectureRepository;
	private final ExternalActivityRepository externalActivityRepository;
	private final LectureStudyRepository lectureStudyRepository;
	private final ExternalActivityStudyRepository externalActivityStudyRepository;
	private final StudyRepository studyRepository;
	private final StudyRelationRepository studyRelationRepository;
	private final ScrapRepository scrapRepository;
	private final SmsService smsService;
	private final MemberUtil memberUtil;


	public StudyUpdateResponse updateStudy(final StudyUpdateRequest studyUpdateRequest, final Long studyId) {
		final Member member = memberUtil.getCurrentMember();

		Study study =
			studyRepository
				.findByMemberAndId(member, studyId)
				.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		study.updateStudy(
			studyUpdateRequest.title(),
			studyUpdateRequest.content(),
			studyUpdateRequest.recruitmentCount(),
			studyUpdateRequest.method(),
			studyUpdateRequest.frequency(),
			studyUpdateRequest.kakaoLink(),
			studyUpdateRequest.questionLink(),
			studyUpdateRequest.recruitmentStartAt(),
			studyUpdateRequest.recruitmentEndAt());
		return StudyUpdateResponse.from(study);
	}

	public void deleteStudy(final Long studyId) {
		Study study = studyRepository.findById(studyId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		final Member loginMember = memberUtil.getCurrentMember();

		if (!loginMember.equals(study.getMember())) {
			throw new CustomException(ErrorCode.STUDY_CANNOT_DELETED);
		}

		List<StudyRelation> allStudyApplicants = studyRelationRepository.findAllByStudyAndStatusNot(study, StudyMatchingStatus.CANCEL);
		allStudyApplicants.forEach(this::sendStudyDeletionAlarmToStudyApplicant);

		if (StudyType.LECTURE.equals(study.getType())) {
			LectureStudy targetLectureStudy = lectureStudyRepository.findByStudy(study)
				.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_STUDY_NOT_FOUND));

			lectureStudyRepository.delete(targetLectureStudy);
			studyRepository.delete(study);
		}

		if (StudyType.EXTERNAL_ACTIVITY.equals(study.getType())) {
			ExternalActivityStudy targetExternalActivityStudy = externalActivityStudyRepository.findByStudy(study)
				.orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_ACTIVITY_STUDY_NOT_FOUND));

			externalActivityStudyRepository.delete(targetExternalActivityStudy);
			studyRepository.delete(study);
		}

	}

	private void sendStudyDeletionAlarmToStudyApplicant(StudyRelation studyRelation) {
		Member studyApplicant = studyRelation.getMember();
		Study studyPost = studyRelation.getStudy();
		smsService.sendFormattedSms(
			studyApplicant.getPhoneNumber(),
			MESSAGE_ALARM_SEJONGPEER_PREFIX +
			MESSAGE_ALARM_PARENTHESES_PREFIX +
			studyPost.getTitle().substring(0,10) +
			MESSAGE_ALARM_PARENTHESES_POSTFIX +
			SmsText.STUDY_POST_DELETION_ALARM.getValue()
		);
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
			int scrapCount = getScrapCountByStudy(study);
			if (studyType == StudyType.LECTURE) {
				return StudyTotalPostResponse.fromLectureStudy(study, categoryName, scrapCount);
			} else if (studyType == StudyType.EXTERNAL_ACTIVITY) {
				return StudyTotalPostResponse.fromExternalActivityStudy(study, categoryName, scrapCount);
			} else {
				throw new CustomException(ErrorCode.STUDY_TYPE_NOT_FOUND);
			}
		});
	}

	@Transactional(readOnly = true)
	public StudyPostInfoResponse getOneStudyPostInfo(final Long studyId) {
		final Member loginMember = memberUtil.getCurrentMember();

		Study study = studyRepository.findById(studyId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		String categoryName = getCategoryNameByStudyType(study);

		int scrapCount = getScrapCountByStudy(study);

		boolean isApplied = studyRelationRepository.existsByMemberAndStudyAndStatusNot(loginMember, study, StudyMatchingStatus.CANCEL);

		boolean isScraped = scrapRepository.existsByMemberAndStudy(loginMember, study);

		return StudyPostInfoResponse.fromStudy(study, categoryName, scrapCount, isApplied, isScraped);
	}

	public String getCategoryNameByStudyType(Study study) {
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
	public List<StudyTotalPostResponse> getAllStudyPostBySearch(StudyType studyType, Integer page, Integer size, StudyPostSearchRequest request) {
		Specification<Study> spec = Specification.where(StudySpecification.checkStudyTypeMatching(studyType))
			.and(StudySpecification.checkRecruitmentPersonnelMatch(request.getRecruitmentPersonnel()))
			.and(StudySpecification.findByRecruitmentStatus(request.getIsRecruiting()))
			.and(StudySpecification.containsTitleOrContent(request.getSearchWord()));

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<Study> studyPage = studyRepository.findAll(spec, pageable);

		return studyPage.stream()
			.filter(study -> isCategoryNameMatching(study, request.getCategoryId()))
			.map(this::mapToCommonStudyTotalPostResponse)
			.collect(Collectors.toUnmodifiableList());

	}

	private boolean isCategoryNameMatching(Study study, Long categoryId) {
		if (categoryId == null) return true;

		if (study.getType().equals(StudyType.LECTURE)) {
			Lecture lecture = lectureRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));
			String lectureName = lecture.getName();

			LectureStudy lectureStudy = lectureStudyRepository.findByStudy(study)
				.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_AND_STUDY_NOT_CONNECTED));
			String studyCategoryName = lectureStudy.getLecture().getName();

			return lectureName.equals(studyCategoryName);
		} else {
			ExternalActivity externalActivity = externalActivityRepository.findById(categoryId)
				.orElseThrow(() -> new CustomException(ErrorCode.EXTERNAL_ACTIVITY_NOT_FOUND));
			String externalActivityName = externalActivity.getName();

			ExternalActivityStudy externalActivityStudy = externalActivityStudyRepository.findByStudy(study)
				.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_AND_STUDY_NOT_CONNECTED));
			String studyCategoryName = externalActivityStudy.getExternalActivity().getName();

			return externalActivityName.equals(studyCategoryName);
		}
	}

	public StudyTotalPostResponse mapToCommonStudyTotalPostResponse(Study study) {
		String categoryName = getCategoryNameByStudyType(study);
		int scrapCount = getScrapCountByStudy(study);
		if (study.getType() == StudyType.LECTURE) {
			return StudyTotalPostResponse.fromLectureStudy(study, categoryName, scrapCount);
		} else if (study.getType() == StudyType.EXTERNAL_ACTIVITY) {
			return StudyTotalPostResponse.fromExternalActivityStudy(study, categoryName, scrapCount);
		} else {
			throw new CustomException(ErrorCode.STUDY_TYPE_NOT_FOUND);
		}
	}

	public int getScrapCountByStudy(Study study) {
		Long scrapCount = scrapRepository.countByStudy(study);
		return scrapCount.intValue();
	}
}
