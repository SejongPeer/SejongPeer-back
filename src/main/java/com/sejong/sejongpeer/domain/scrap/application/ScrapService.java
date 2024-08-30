package com.sejong.sejongpeer.domain.scrap.application;

import java.util.List;
import java.util.stream.Collectors;

import com.sejong.sejongpeer.domain.scrap.dto.response.StudyScrapCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyTotalPostResponse;
import com.sejong.sejongpeer.domain.study.service.StudyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.member.entity.Member;
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
	private final StudyService studyService;
	private final MemberUtil memberUtil;

	public Long getScrapCountByStudyPost(final Long studyId) {
		Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		return scrapRepository.countByStudy(study);
	}

	public StudyScrapCreateResponse createScrap(Long studyId) {
		Study study = studyRepository.findById(studyId).orElseThrow(
			() -> new CustomException(ErrorCode.STUDY_NOT_FOUND)
		);

		Scrap newScrap = Scrap.createScrap(ScrapType.STUDY, memberUtil.getCurrentMember(), study);
		scrapRepository.save(newScrap);

		return StudyScrapCreateResponse.from(newScrap);
	}

	public void deleteScrap(Long scrapId) {
		Scrap scrap = scrapRepository.findById(scrapId).orElseThrow(
			() -> new CustomException(ErrorCode.SCRAP_NOT_FOUND)
		);

		scrapRepository.delete(scrap);
	}

	public List<StudyTotalPostResponse> getAllMyScrapStudyPosts() {
		final Member loginMember = memberUtil.getCurrentMember();

		List<Scrap> myAllScraps = scrapRepository.findAllByMember(loginMember);

		return myAllScraps.stream()
			.map(Scrap::getStudy)
			.map(studyService::mapToCommonStudyTotalPostResponse)
			.collect(Collectors.toUnmodifiableList());

	}
}
