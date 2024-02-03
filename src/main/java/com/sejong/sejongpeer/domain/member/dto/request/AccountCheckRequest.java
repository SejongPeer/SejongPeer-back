package com.sejong.sejongpeer.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AccountCheckRequest(@NotBlank(message = "계정은 비워둘 수 없습니다.") String account) {}
