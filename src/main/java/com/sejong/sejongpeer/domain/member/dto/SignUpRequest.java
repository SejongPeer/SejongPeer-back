package com.sejong.sejongpeer.domain.member.dto;

import java.time.LocalDate;

import com.sejong.sejongpeer.domain.member.entity.type.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

public record SignUpRequest(
	@NotBlank(message = "계정은 비워둘 수 없습니다.") String account,
	@NotBlank(message = "비밀번호는 비워둘 수 없습니다.") String password,
	@NotBlank(message = "비밀번호 확인 칸은 비워둘 수 없습니다.") String passwordCheck,
	@Email(message = "올바른 형식이 아닙니다.") String email,
	@NotBlank(message = "이름은 비워둘 수 없습니다.") String name,
	@NotBlank(message = "학번은 비워둘 수 없습니다.") String studentId,
	@NotBlank(message = "전공은 비워둘 수 없습니다.") String major,
	@Positive(message = "학년은 비워둘 수 없습니다.") Integer grade,
	@NotNull(message = "성별은 비워둘 수 없습니다.") Gender gender,
	@Past(message = "생일은 비워둘 수 없습니다.") LocalDate birthday,
	@NotBlank(message = "전화번호는 비워둘 수 없습니다.") String phoneNumber) {
}
