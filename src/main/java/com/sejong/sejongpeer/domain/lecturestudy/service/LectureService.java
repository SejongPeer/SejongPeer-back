package com.sejong.sejongpeer.domain.lecturestudy.service;

import com.sejong.sejongpeer.domain.lecturestudy.dto.CollegeLessonProfessorResponse;
import com.sejong.sejongpeer.domain.lecturestudy.entity.Lecture;
import com.sejong.sejongpeer.domain.lecturestudy.repository.LectureRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureService {

	private final LectureRepository lectureRepository;

	@Transactional(readOnly = true)
	public List<CollegeLessonProfessorResponse> getLessonInfoByColleage(String college) {
		List<Lecture> lectures = lectureRepository.findAllByCollege(college);
		return Collections.unmodifiableList(
			lectures.stream()
				.map(CollegeLessonProfessorResponse::from)
				.collect(Collectors.toList())
		);
	}
}
