package com.sejong.sejongpeer.domain.studyrelation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.domain.scrap.application.ScrapService;
import com.sejong.sejongpeer.domain.study.dto.response.StudyApplicantsListRespone;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyMatchingRequest;
import com.sejong.sejongpeer.global.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.study.service.TagService;
import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyApplyRequest;
import com.sejong.sejongpeer.domain.studyrelation.dto.response.AppliedStudyResponse;
import com.sejong.sejongpeer.domain.studyrelation.dto.response.StudyRelationCreateResponse;
import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;
import com.sejong.sejongpeer.domain.studyrelation.repository.StudyRelationRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;
import com.sejong.sejongpeer.infra.sms.service.SmsService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRelationService {
	private static final String MESSAGE_ALARM_SEJONGPEER_PREFIX = "[세종피어] ";
	private static final String MESSAGE_ALARM_PARENTHESES_PREFIX = "(";
	private static final String MESSAGE_ALARM_PARENTHESES_POSTFIX = "...) ";

	private final StudyRepository studyRepository;
	private final SmsService smsService;
	private final StudyRelationRepository studyRelationRepository;
	private final MemberRepository memberRepository;
	private final MemberUtil memberUtil;
	private final SecurityUtil securityUtil;
	private final TagService tagService;
	private final ScrapService scrapService;

	public StudyRelationCreateResponse applyStudy(StudyApplyRequest studyApplyRequest) {
		Study study = studyRepository.findById(studyApplyRequest.studyId())
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		final Member loginMember = memberUtil.getCurrentMember();

		StudyRelation lastRelation = studyRelationRepository.findTopByMemberAndStudyOrderByIdDesc(loginMember, study)
			.orElse(null);

		if (lastRelation != null) {
			if (lastRelation.getCanceledAt() != null &&
				lastRelation.getCanceledAt().isAfter(LocalDateTime.now().minusHours(1))) {
				throw new CustomException(ErrorCode.CANNOT_REAPPLY_WITHIN_AN_HOUR);
			}

			if (!lastRelation.getStatus().equals(StudyMatchingStatus.CANCEL)) {
				throw new CustomException(ErrorCode.DUPLICATED_STUDY_APPLICATION);
			}
		}

		StudyRelation newStudyapplication = StudyRelation.createStudyRelations(loginMember,study);

		sendStudyApplicantAlarmToStudyWriter(newStudyapplication);

		studyRelationRepository.save(newStudyapplication);

		return StudyRelationCreateResponse.from(newStudyapplication);
	}

	private void sendStudyApplicantAlarmToStudyWriter(StudyRelation newStudyApplicaitonHistory) {
		smsService.sendSms(newStudyApplicaitonHistory.getStudy().getMember().getPhoneNumber(), SmsText.STUDY_APPLY_ALARM);
	}

	public void deleteStudyApplicationHistory(final Long studyId) {
		final String loginMemberId = securityUtil.getCurrentMemberId();

		StudyRelation studyApplicationHistory = studyRelationRepository.findTopByMemberIdAndStudyIdOrderByIdDesc(loginMemberId, studyId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_RELATION_NOT_FOUND));

		studyApplicationHistory.registerCanceledAt(LocalDateTime.now());
		studyApplicationHistory.changeStudyMatchingStatus(StudyMatchingStatus.CANCEL);
		studyRelationRepository.save(studyApplicationHistory);

	}

	public Map<String, Boolean> updateStudyMatchingStatus(StudyMatchingRequest request) {
		Member studyApplicant = memberRepository.findByNickname(request.applicantNickname())
			.orElseThrow(() -> new CustomException(ErrorCode.NICKNAME_IS_NULL));

		StudyRelation studyResume = studyRelationRepository.findTopByMemberIdAndStudyIdOrderByIdDesc(studyApplicant.getId(), request.studyId())
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_APPLY_HISTORY_NOT_FOUND));

		boolean isFulleApplication = false;
		Map<String, Boolean> response = new HashMap<>();

		if (studyResume.getStatus().equals(StudyMatchingStatus.CANCEL)) {
			throw new CustomException(ErrorCode.INVALID_STUDY_MATHCING_STATUS_UPDATE_CONDITION);
		}

		Study appliedStudy = studyResume.getStudy();

		if (request.isAccept()) {
			if (appliedStudy.getRecruitmentCount() <= appliedStudy.getParticipantsCount()) {
				throw new CustomException(ErrorCode.STUDY_APPLICANT_CANNOT_BE_ACCEPTED);
			}

			studyResume.changeStudyMatchingStatus(StudyMatchingStatus.ACCEPT);
			appliedStudy.addParticipantsCount();
			studyRepository.save(appliedStudy);

			if (appliedStudy.getRecruitmentCount() <= appliedStudy.getParticipantsCount()) {
				appliedStudy.changeStudyRecruitmentStatus(RecruitmentStatus.CLOSED);
				studyRepository.save(appliedStudy);

				List<StudyRelation> appliedStudyHistory = studyRelationRepository.findAllByStudyAndStatus(appliedStudy, StudyMatchingStatus.ACCEPT);
				appliedStudyHistory.forEach(this::sendStudyKakaoLink);

				isFulleApplication = true;
			}
		} else {
			studyResume.changeStudyMatchingStatus(StudyMatchingStatus.REJECT);
			sendStudyRejectAlarmToStudyApplicant(studyResume);
		}

		studyRelationRepository.save(studyResume);
		response.put("isFull", isFulleApplication);

		return response;
	}

	private void sendStudyRejectAlarmToStudyApplicant(StudyRelation studyRelation) {
		Member studyRejectedApplicant = studyRelation.getMember();
		Study studyPost = studyRelation.getStudy();
		String title = studyPost.getTitle();
		String subTitle = title.length() > 10 ? title.substring(0, 10) : title;

		String formattedMessage = MESSAGE_ALARM_PARENTHESES_PREFIX +
			subTitle + MESSAGE_ALARM_PARENTHESES_POSTFIX + SmsText.STUDY_APPLY_REJECT_ALARM.getValue();
		smsService.sendFormattedSms(studyRejectedApplicant.getPhoneNumber(), formattedMessage);
	}

	public void earlyCloseRegistration(final Long studyId) {

		final Member member = memberUtil.getCurrentMember();

		Study study =
			studyRepository
				.findByMemberAndId(member, studyId)
				.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		if (study.getRecruitmentStatus().equals(RecruitmentStatus.CLOSED)) {
			throw new CustomException(ErrorCode.STUDY_ALREADY_CLOSED);
		}

		study.changeStudyRecruitmentStatus(RecruitmentStatus.CLOSED);

		List<StudyRelation> studyRelations = studyRelationRepository.findByStudyId(studyId);
		studyRelations.forEach(studyRelation -> {
			if (studyRelation.getStatus() == StudyMatchingStatus.ACCEPT) {
				sendStudyKakaoLink(studyRelation);
			} else {
				studyRelation.changeStudyMatchingStatus(StudyMatchingStatus.REJECT);
			}
		});
		studyRelationRepository.saveAll(studyRelations);
	}

	private void sendStudyKakaoLink(StudyRelation studyRelation){
		String title = studyRelation.getStudy().getTitle();
		String subTitle = title.length() > 10 ? title.substring(0, 10) : title;
		smsService.sendFormattedSms(
			studyRelation.getMember().getPhoneNumber(),
			MESSAGE_ALARM_SEJONGPEER_PREFIX +
				MESSAGE_ALARM_PARENTHESES_PREFIX +
				subTitle +
				MESSAGE_ALARM_PARENTHESES_POSTFIX +
				SmsText.STUDY_RECRUITMENT_COMPLETED.getValue() +
				studyRelation.getStudy().getKakaoLink()
		);
	}

	public List<AppliedStudyResponse> getAppliedStudies() {
		final Member loginMember = memberUtil.getCurrentMember();
		List<StudyRelation> studyRelations = loginMember.getStudyRelations();

		studyRelations.sort((sr1, sr2) -> sr2.getStudy().getId().compareTo(sr1.getStudy().getId()));

		List<AppliedStudyResponse> list = new ArrayList<>();
		studyRelations.stream()
			.forEach(studyRelation -> {
				if(!studyRelation.getStatus().equals(StudyMatchingStatus.CANCEL)) {
					Study study = studyRelation.getStudy();

					Long scrapCount = scrapService.getScrapCountByStudyPost(study.getId());
					List<String> tags = tagService.getTagsNameByStudy(study);
					boolean hasMemberScrappedStudy = scrapService.hasMemberScrappedStudy(loginMember, study);

					list.add(AppliedStudyResponse.of(study, tags, scrapCount, hasMemberScrappedStudy));
				}
			});
		return list;
	}

	public Map<String, List<StudyApplicantsListRespone>> getApplicatnsList() {
		final Member member = memberUtil.getCurrentMember();
		List<Study> studyList = member.getStudies();

		studyList.sort((s1, s2) -> s2.getId().compareTo(s1.getId()));

		List<StudyApplicantsListRespone> lectureList = new ArrayList<>();
		List<StudyApplicantsListRespone> externalList = new ArrayList<>();

		studyList.stream()
			.forEach(study -> {
				List<StudyRelation> relations = study.getStudyRelations();
				StudyApplicantsListRespone response = StudyApplicantsListRespone.of(study, relations);

				if (study.getType().equals(StudyType.LECTURE)) {
					lectureList.add(response);
				}
				else {
					externalList.add(response);
				}
			});

		Map<String, List<StudyApplicantsListRespone>> responseMap = new HashMap<>();
		responseMap.put("lecture", lectureList);
		responseMap.put("external", externalList);

		return responseMap;
	}
}
