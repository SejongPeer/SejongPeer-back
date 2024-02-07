package com.sejong.sejongpeer.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordResetRequest(
        @NotBlank(message = "학번은 비워둘 수 없습니다.") String studentId,
        @NotBlank(message = "계정은 비워둘 수 없습니다.") String account,
        @NotBlank(message = "변경할 비밀번호는 비워둘 수 없습니다.") String password,
        @NotBlank(message = "비밀번호 확인 칸은 비워둘 수 없습니다.") String passwordCheck) {}
