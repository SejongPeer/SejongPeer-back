package com.sejong.sejongpeer.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountFindRequest(
	@NotBlank(message = "학번은 필수 입력값입니다.") String studentId,
	@NotBlank(message = "이름은 필수 입력값입니다.")
	@Size(min = 2, message = "이름은 최소 2자 이상 입력해주세요.") String name) {
}
