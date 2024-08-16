package com.sejong.sejongpeer.domain.college.service;

import com.sejong.sejongpeer.domain.college.dto.CollegeMajorResponse;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CollegeMajorService {

	private final CollegeMajorRepository collegeMajorRepository;

	@Cacheable(cacheNames = "getAllColleges", key = "'CollegeMajor'", cacheManager = "redisCacheManager")
	public List<String> getAllColleges() {
		return collegeMajorRepository.findAllColleges();
	}

	@Transactional(readOnly = true)
	public List<CollegeMajorResponse> getAllMajorsByCollege(String college) {
		return collegeMajorRepository.findAllByCollege(college).stream()
			.map(cm -> new CollegeMajorResponse(cm.getCollege(), cm.getMajor()))
			.toList();
	}
}
