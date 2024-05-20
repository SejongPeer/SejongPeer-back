package com.sejong.sejongpeer.domain.scrap.application;

import org.hibernate.internal.SessionCreationOptions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.scrap.dao.ScrapRepository;
import com.sejong.sejongpeer.domain.scrap.entity.Scrap;
import com.sejong.sejongpeer.domain.scrap.entity.ScrapType;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {

	private final ScrapRepository scrapRepository;
	private final StudyRepository studyRepository;
	private final MemberUtil memberUtil;

	public Long createScrap(Long studyId) {
		Study study = studyRepository.findById(studyId).orElseThrow(
			() -> new CustomException(ErrorCode.STUDY_NOT_FOUND)
		);

		Scrap scrap = Scrap.createScrap(ScrapType.STUDY, memberUtil.getCurrentMember(), study);
		return scrapRepository.save(scrap).getId();
	}
}
