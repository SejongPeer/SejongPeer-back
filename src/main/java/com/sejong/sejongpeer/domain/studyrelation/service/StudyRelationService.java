package com.sejong.sejongpeer.domain.studyrelation.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.domain.study.dto.response.StudyApplicantsListRespone;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
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

	private final StudyRepository studyRepository;
	private final SmsService smsService;
	private final StudyRelationRepository studyRelationRepository;
	private final MemberRepository memberRepository;
	private final MemberUtil memberUtil;
	private final SecurityUtil securityUtil;
	private final TagService tagService;

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

	public void updateStudyMatchingStatus(StudyMatchingRequest request) {
		Member studyApplicant = memberRepository.findByNickname(request.applicantNickname())
			.orElseThrow(() -> new CustomException(ErrorCode.NICKNAME_IS_NULL));

		StudyRelation studyResume = studyRelationRepository.findTopByMemberIdAndStudyIdOrderByIdDesc(studyApplicant.getId(), request.studyId())
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_APPLY_HISTORY_NOT_FOUND));

		if (studyResume.getStatus().equals(StudyMatchingStatus.CANCEL)) {
			throw new CustomException(ErrorCode.INVALID_STUDY_MATHCING_STATUS_UPDATE_CONDITION);
		}

		if (request.isAccept()) {
			studyResume.changeStudyMatchingStatus(StudyMatchingStatus.ACCEPT);
			Study appliedStudy = studyResume.getStudy();
			appliedStudy.addParticipantsCount();
			studyRepository.save(appliedStudy);
		} else {
			studyResume.changeStudyMatchingStatus(StudyMatchingStatus.REJECT);
			sendStudyRejectAlarmToStudyApplicant(studyResume);
		}

		studyRelationRepository.save(studyResume);
	}

	private void sendStudyRejectAlarmToStudyApplicant(StudyRelation studyRelation) {
		Member studyRejectedApplicant = studyRelation.getMember();
		Study studyPost = studyRelation.getStudy();
		String formattedMessage = "[" + studyPost.getTitle().substring(0, 2) + "...]" + SmsText.STUDY_APPLY_REJECT_ALARM.getValue();
		smsService.sendFormattedSms(studyRejectedApplicant.getPhoneNumber(), formattedMessage);
	}

	public void earlyCloseRegistration(final Long studyId) {
		List<StudyRelation> studyRelations = studyRelationRepository.findByStudyId(studyId);

		if (studyRelations.isEmpty()) {
			throw new CustomException(ErrorCode.STUDY_RELATION_NOT_FOUND);
		}

		studyRelations.forEach(study -> {
			study.getStudy().changeStudyRecruitmentStatus(RecruitmentStatus.CLOSED);
			if (study.getStatus() == StudyMatchingStatus.ACCEPT) {
				sendStudyKakaoLink(study);
			} else {
				study.changeStudyMatchingStatus(StudyMatchingStatus.REJECT);
			}
		});
		studyRelationRepository.saveAll(studyRelations);
	}

	private void sendStudyKakaoLink(StudyRelation study){
		smsService.sendSms(
			study.getMember().getPhoneNumber(),
			SmsText.valueOf(SmsText.STUDY_RECRUITMENT_COMPLETED + study.getStudy().getKakaoLink())
		);
	}

	public List<AppliedStudyResponse> getAppliedStudies() {
		final Member loginMember = memberUtil.getCurrentMember();
		List<StudyRelation> studyRelations = loginMember.getStudyRelations();


		List<AppliedStudyResponse> list = new ArrayList<>();
		studyRelations.stream()
			.forEach(studyRelation -> {
				if(!studyRelation.getStatus().equals(StudyMatchingStatus.CANCEL)) {
					Study study = studyRelation.getStudy();
					List<String> tags = tagService.getTagsNameByStudy(study);
					list.add(AppliedStudyResponse.of(study, tags));
				}
			});
		return list;
	}

	public List<StudyApplicantsListRespone> getApplicatnsList() {
		final Member member = memberUtil.getCurrentMember();
		List<Study> studyList = member.getStudies();


		List<StudyApplicantsListRespone> respones = new ArrayList<>();

		studyList.stream()
			.forEach(study -> {
				List<StudyRelation> relations = study.getStudyRelations();
				respones.add(StudyApplicantsListRespone.of(study, relations));
			});

		return respones;
	}
}
