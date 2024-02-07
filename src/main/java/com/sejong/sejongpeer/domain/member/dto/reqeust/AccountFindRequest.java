package com.sejong.sejongpeer.domain.member.dto.reqeust;

import com.sejong.sejongpeer.domain.member.type.AccountFindOption;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountFindRequest(
        @NotNull(message = "계정 찾기 시 인증할 수단을 선택해주세요") AccountFindOption option,
        @NotBlank(message = "학번은 필수 입력값입니다.") String studentId,
        @Pattern(regexp = "^010[0-9]{7}$", message = "올바른 전화번호를 입력해주세요.") String phoneNumber,
        @Size(min = 2, message = "이름은 최소 2자 이상 입력해주세요.") String name) {}
