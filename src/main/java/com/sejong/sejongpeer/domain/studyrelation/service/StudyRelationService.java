package com.sejong.sejongpeer.domain.studyrelation.service;

import java.util.ArrayList;
import java.util.List;

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
	private final MemberUtil memberUtil;
	private final TagService tagService;

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

	public List<AppliedStudyResponse> getAppliedStudies() {
		final Member loginMember = memberUtil.getCurrentMember();
		List<StudyRelation> studyRelations = loginMember.getStudyRelations();


		List<AppliedStudyResponse> list = new ArrayList<>();
		studyRelations.stream()
			.forEach(studyRelation -> {
				Study study = studyRelation.getStudy();
				List<String> tags = tagService.getTagsNameByStudy(study);
				list.add(AppliedStudyResponse.of(study, tags));
			});
		return list;
	}
}
