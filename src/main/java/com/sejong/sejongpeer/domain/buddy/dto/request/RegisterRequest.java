package com.sejong.sejongpeer.domain.buddy.dto.request;

import com.sejong.sejongpeer.domain.buddy.entity.type.*;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
	@NotNull(message = "선호 성별을 선택해주세요.") GenderOption genderOption,
	@NotNull(message = "선호 학생타입을 선택해주세요.") BuddyType buddyType,
	@NotNull(message = "선호하는 학부를 선택해주세요.") BuddyRange buddyRange,
	@NotNull(message = "선호하는 학년을 선택해주세요.") GradeOption gradeOption,
	@NotNull(message = "복수전공 여부를 선택해주세요.") Boolean isSubMajor
) {}
