package com.sejong.sejongpeer.domain.externalactivity.service;

import com.sejong.sejongpeer.domain.externalactivity.dto.ExternalActivityCategoryResponse;
import com.sejong.sejongpeer.domain.externalactivity.entity.ExternalActivity;
import com.sejong.sejongpeer.domain.externalactivity.repository.ExternalActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ExternalActivityService {

	private final ExternalActivityRepository externalActivityRepository;

	@Cacheable(cacheNames = "getAllExternalActivityCategories", key = "#root.target + #root.methodName", sync = true, cacheManager = "redisCacheManager")
	public List<ExternalActivityCategoryResponse> getAllExternalActivityCategories() {
		List<ExternalActivity> externalActivityList = externalActivityRepository.findAll();

		return externalActivityList.stream()
			.map(ExternalActivityCategoryResponse::from)
			.collect(Collectors.toUnmodifiableList());
	}
}
