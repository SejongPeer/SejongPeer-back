package com.sejong.sejongpeer.domain.lecture.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.lecture.dto.CollegeLectureProfessorResponse;
import com.sejong.sejongpeer.domain.lecture.entity.Lecture;
import com.sejong.sejongpeer.domain.lecture.repository.LectureRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureService {

	private final LectureRepository lectureRepository;

	@Transactional(readOnly = true)
	public List<CollegeLectureProfessorResponse> getLessonInfoByColleage(String college) {
		List<Lecture> lectures = lectureRepository.findAllByCollege(college);
		return Collections.unmodifiableList(
			lectures.stream()
				.map(CollegeLectureProfessorResponse::from)
				.collect(Collectors.toList())
		);
	}
}
