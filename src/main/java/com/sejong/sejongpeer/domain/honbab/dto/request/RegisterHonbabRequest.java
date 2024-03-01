package com.sejong.sejongpeer.domain.honbab.dto.request;

import java.time.Duration;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.GenderOption;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.MenuCategoryOption;

import jakarta.validation.constraints.NotNull;

public record RegisterHonbabRequest(
	@NotNull(message = "선호 성별을 선택해주세요.") GenderOption genderOption,
	@NotNull(message = "원하는 메뉴 카테고리를 선택해주세요.") MenuCategoryOption menuCategoryOption,
	@NotNull(message = "매칭 대기 시간을 선택해주세요.") Duration waitTime
) {}
