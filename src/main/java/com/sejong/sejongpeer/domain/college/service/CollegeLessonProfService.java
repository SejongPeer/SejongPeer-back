package com.sejong.sejongpeer.domain.college.service;

import com.sejong.sejongpeer.domain.college.dto.CollegeLessonProfResponse;
import com.sejong.sejongpeer.domain.college.repository.CollegeLessonProfRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CollegeLessonProfService {

	private final CollegeLessonProfRepository collegeLessonProfRepository;

	public List<CollegeLessonProfResponse> getLessonInfoByColleage(String college) {
		return collegeLessonProfRepository.findAllByCollege(college).stream()
			.map(collegeLessonProf -> new CollegeLessonProfResponse(collegeLessonProf.getLesson(), collegeLessonProf.getProf()))
			.collect(Collectors.toList());
	}
}
