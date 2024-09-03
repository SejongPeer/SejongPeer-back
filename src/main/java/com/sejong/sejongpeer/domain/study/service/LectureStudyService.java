package com.sejong.sejongpeer.domain.study.service;

import com.sejong.sejongpeer.domain.image.dto.request.StudyImageUploadRequest;
import com.sejong.sejongpeer.domain.image.dto.response.StudyImageUrlResponse;
import com.sejong.sejongpeer.domain.image.entity.Image;
import com.sejong.sejongpeer.domain.image.repository.ImageRepository;
import com.sejong.sejongpeer.domain.image.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.lecture.entity.Lecture;
import com.sejong.sejongpeer.domain.lecture.repository.LectureRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.domain.study.dto.request.LectureStudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.entity.LectureStudy;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.LectureStudyRepository;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.study.vo.StudyVo;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureStudyService {

	private final SecurityUtil securityUtil;

	private final StudyRepository studyRepository;
	private final LectureRepository lectureRepository;
	private final LectureStudyRepository lectureStudyRepository;
	private final MemberRepository memberRepository;

	private final TagService tagService;
	private final StudyService studyService;

	public StudyCreateResponse createStudy(LectureStudyCreateRequest request) throws IOException {
		final String memberId = securityUtil.getCurrentMemberId();

		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Lecture lecture = lectureRepository.findById(request.lectureId())
			.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

		StudyVo vo = StudyVo.from(request);
		Study study = Study.create(member, vo);
		Study saveStudy = studyRepository.save(study);

		List<StudyImageUrlResponse> lectureStudyImageUrlResponse = studyService.uploadFiles(saveStudy.getId(), request.base64ImagesList());

		if (lectureStudyImageUrlResponse.isEmpty()) {
			studyRepository.delete(saveStudy);
			throw new CustomException(ErrorCode.STUDY_IMAGE_SIZE_TOO_BIG);
		}

		tagService.setTagAndStudyTagMap(vo.tags(), saveStudy);

		LectureStudy lectureStudy = LectureStudy.create(lecture, saveStudy);

		lectureStudyRepository.save(lectureStudy);

		return StudyCreateResponse.from(saveStudy, lectureStudyImageUrlResponse);
	}
}
