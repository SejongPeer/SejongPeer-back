package com.sejong.sejongpeer.domain.honbab.dto.request;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.GenderOption;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.MenuCategoryOption;

import jakarta.validation.constraints.NotNull;

public record RegisterHonbab(
	@NotNull(message = "선호 성별을 선택해주세요.") GenderOption genderOption,
	@NotNull(message = "원하는 메뉴 카테고리를 선택해주세요.") MenuCategoryOption menuCategoryOption
) {
}
