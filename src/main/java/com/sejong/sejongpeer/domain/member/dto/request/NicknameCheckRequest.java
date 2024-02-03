package com.sejong.sejongpeer.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;

public record NicknameCheckRequest(
        @Pattern(regexp = "^[a-zA-Z가-힣0-9]{2,8}$", message = "닉네임은 2자 이상 8자 이하 한글, 영어, 숫자만 입력해주세요.")
                String nickname) {}
