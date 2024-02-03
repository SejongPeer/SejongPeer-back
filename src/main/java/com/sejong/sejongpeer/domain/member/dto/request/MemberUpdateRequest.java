package com.sejong.sejongpeer.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberUpdateRequest(
        @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,8}$", message = "닉네임은 2자 이상 8자 이하 한글, 영어, 숫자만 입력해주세요.")
                String nickname,
        @NotBlank(message = "기존 비밀번호를 입력해주세요.") String currentPassword,
        @Pattern(
                        regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}\\[\\]:;<>,.?/~\\\\-]{8,20}$",
                        message = "최소 8자, 최대 20자의 영문자, 숫자, 특수문자로만 이루어져야합니다.")
                String newPassword,
        @Pattern(
                        regexp = "^[a-zA-Z0-9!@#$%^&*()_+{}\\[\\]:;<>,.?/~\\\\-]{8,20}$",
                        message = "최소 8자, 최대 20자의 영문자, 숫자, 특수문자로만 이루어져야합니다.")
                String newPasswordCheck) {}
