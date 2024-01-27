package com.sejong.sejongpeer.domain.college.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.college.dto.CollegeMajorResponse;
import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CollegeMajorService {

	private final CollegeMajorRepository collegeMajorRepository;

	public List<String> getAllColleges() {
		return collegeMajorRepository.findAllColleges();
	}

	public List<CollegeMajorResponse> getAllMajorsByCollege(String college) {
		return collegeMajorRepository.findAllByCollege(college).stream()
			.map(cm -> new CollegeMajorResponse(cm.getCollege(), cm.getMajor()))
			.toList();
	}
}
