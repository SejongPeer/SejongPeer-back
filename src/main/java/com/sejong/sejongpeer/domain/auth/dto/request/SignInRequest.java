package com.sejong.sejongpeer.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank(message = "아이디를 입력해주세요.") String account,
        @NotBlank(message = "비밀번호를 입력해주세요.") String password) {}
