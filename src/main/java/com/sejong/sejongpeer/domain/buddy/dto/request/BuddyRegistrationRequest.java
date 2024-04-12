package com.sejong.sejongpeer.domain.buddy.dto.request;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.ClassTypeOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.CollegeMajorOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.GenderOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.GradeOption;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BuddyRegistrationRequest(
	@NotNull(message = "선호 성별을 선택해주세요.") GenderOption genderOption,
	@NotNull(message = "선호 학생타입을 선택해주세요.") ClassTypeOption classTypeOption,
	@NotNull(message = "선호하는 학부를 선택해주세요.") CollegeMajorOption collegeMajorOption,
	@NotNull(message = "선호하는 학년을 선택해주세요.") GradeOption gradeOption,
	@NotNull(message = "복수전공 여부를 선택해주세요.") Boolean isSubMajor
) {}
