package com.sejong.sejongpeer.domain.studyrelation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.studyrelation.dto.response.StudyRelationCreateResponse;
import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;
import com.sejong.sejongpeer.domain.studyrelation.repository.StudyRelationRepository;
import com.sejong.sejongpeer.infra.sms.service.SmsService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyRelationService {

	private final SmsService smsService;
	private final StudyRelationRepository studyRelationRepository;

	public StudyRelationCreateResponse createStudyRelation(final Long studyId) {
		return null;
	}

	public void earlyCloseRegistration(final Long studyId) {
		List<StudyRelation> studyRelations = studyRelationRepository.findByStudyId(studyId);

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
