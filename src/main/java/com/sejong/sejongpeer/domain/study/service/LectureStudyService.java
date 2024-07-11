package com.sejong.sejongpeer.domain.study.service;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.lecture.entity.Lecture;
import com.sejong.sejongpeer.domain.lecture.repository.LectureRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.domain.study.dto.request.LectureStudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyFindResponse;
import com.sejong.sejongpeer.domain.study.entity.LectureStudy;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.LectureStudyRepository;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.study.vo.StudyVo;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

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

	public StudyCreateResponse createStudy(LectureStudyCreateRequest request) {
		String memberId = securityUtil.getCurrentMemberId();

		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		Lecture lecture = lectureRepository.findById(request.lectureId())
			.orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

		StudyVo vo = StudyVo.from(request);
		Study study = Study.create(member, vo);
		Study saveStudy = studyRepository.save(study);

		tagService.setTagAndStudyTagMap(vo.tags(), saveStudy);

		LectureStudy lectureStudy = LectureStudy.create(lecture, saveStudy);
		lectureStudyRepository.save(lectureStudy);

		return StudyCreateResponse.from(saveStudy);
	}

	@Transactional(readOnly = true)
	public StudyFindResponse findOneStudy(final Long studyId) {
		Study study =
			studyRepository
				.findById(studyId)
				.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

		return StudyFindResponse.from(study);
	}

	@Transactional(readOnly = true)
	public Slice<StudyFindResponse> findSliceStudy(int size, Long lastId) {
		Slice<Study> studySlice = studyRepository.findStudySlice(size, lastId);
		return studySlice.map(StudyFindResponse::from);
	}

	public void deleteStudy(final Long studyId) {
		studyRepository.deleteById(studyId);
	}
}
