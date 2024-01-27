package com.sejong.sejongpeer.domain.college.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.college.dto.CollegeMajorResponse;
import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CollegeMajorService {

	private final CollegeMajorRepository collegeMajorRepository;

	@Transactional(readOnly = true)
	public List<String> getAllColleges() {
		return collegeMajorRepository.findAllColleges();
	}

	@Transactional(readOnly = true)
	public List<CollegeMajorResponse> getAllMajorsByCollege(String college) {
		return collegeMajorRepository.findAllByCollege(college).stream()
			.map(cm -> new CollegeMajorResponse(cm.getCollege(), cm.getMajor()))
			.collect(Collectors.toUnmodifiableList());
	}
}
