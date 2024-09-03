package com.sejong.sejongpeer.domain.scrap.application;

import java.util.List;
import java.util.stream.Collectors;

import com.sejong.sejongpeer.domain.scrap.dto.response.StudyScrapCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyTotalPostResponse;
import com.sejong.sejongpeer.domain.study.service.StudyService;
import com.sejong.sejongpeer.global.util.SecurityUtil;
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
	private final SecurityUtil securityUtil;

	public Long getScrapCountByStudyPost(final Long studyId) {
		Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		return scrapRepository.countByStudy(study);
	}

	public StudyScrapCreateResponse createScrap(Long studyId) {
		final Member loginMember = memberUtil.getCurrentMember();

		Study study = studyRepository.findById(studyId).orElseThrow(
			() -> new CustomException(ErrorCode.STUDY_NOT_FOUND)
		);

		Scrap existingScrap = scrapRepository.findByMemberAndStudy(loginMember, study)
			.orElse(null);

		if (existingScrap != null) {
			throw new CustomException(ErrorCode.SCRAP_CANNOT_BE_DUPLICATED);
		}

		Scrap newScrap = Scrap.createScrap(ScrapType.STUDY, loginMember, study);
		scrapRepository.save(newScrap);

		return StudyScrapCreateResponse.from(newScrap);
	}

	public void deleteScrap(Long studyId) {
		final String loginMemberId = securityUtil.getCurrentMemberId();

		Scrap scrap = scrapRepository.findByMemberIdAndStudyId(loginMemberId, studyId).orElseThrow(
			() -> new CustomException(ErrorCode.SCRAP_NOT_FOUND)
		);

		scrapRepository.delete(scrap);
	}

	public List<StudyTotalPostResponse> getAllMyScrapStudyPosts() {
		final Member loginMember = memberUtil.getCurrentMember();

		List<Scrap> myAllScraps = scrapRepository.findAllByMember(loginMember);

		return myAllScraps.stream()
			.map(Scrap::getStudy)
			.map(study -> studyService.mapToCommonStudyTotalPostResponse(loginMember, study))
			.collect(Collectors.toUnmodifiableList());
	}

	public boolean hasMemberScrappedStudy(Member member, Study study) {
		return scrapRepository.existsByMemberAndStudy(member, study);
	}
}
