package com.sejong.sejongpeer.domain.studyrelation.service;

import java.util.List;

import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyMatchingRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.studyrelation.dto.request.StudyApplyRequest;
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

	public StudyRelationCreateResponse applyStudy(StudyApplyRequest studyApplyRequest) {
		Study study = studyRepository.findById(studyApplyRequest.studyId())
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		Member loginMember = memberUtil.getCurrentMember();

		List<StudyRelation> studyRelations = studyRelationRepository.findByMemberAndStudy(loginMember, study);
		if (!studyRelations.isEmpty()) {
			throw new CustomException(ErrorCode.DUPLICATED_STUDY_APPLICATION);
		}

		StudyRelation newStudyapplication = StudyRelation.createStudyRelations(loginMember,study);

		studyRelationRepository.save(newStudyapplication);

		return StudyRelationCreateResponse.from(newStudyapplication);
	}

	public void updateStudyMatchingStatus(StudyMatchingRequest request) {
		Member studyApplicant = memberRepository.findByNickname(request.applicantNickname())
			.orElseThrow(() -> new CustomException(ErrorCode.NICKNAME_IS_NULL));

		StudyRelation studyResume = studyRelationRepository.findByMemberIdAndStudyId(studyApplicant.getId(), request.studyId())
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_APPLY_HISTORY_NOT_FOUND));

		if (request.isAccept()) {
			studyResume.changeStudyMatchingStatus(StudyMatchingStatus.ACCEPT);
			Study appliedStudy = studyResume.getStudy();
			appliedStudy.addParticipantsCount();
			studyRepository.save(appliedStudy);
		} else {
			studyResume.changeStudyMatchingStatus(StudyMatchingStatus.REJECT);
		}

		studyRelationRepository.save(studyResume);
	}

	public void earlyCloseRegistration(final Long studyId) {
		List<StudyRelation> studyRelations = studyRelationRepository.findByStudyId(studyId);

		if (studyRelations.isEmpty()) {
			throw new CustomException(ErrorCode.STUDY_RELATION_NOT_FOUND);
		}

		studyRelations.forEach(study -> {
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
}
