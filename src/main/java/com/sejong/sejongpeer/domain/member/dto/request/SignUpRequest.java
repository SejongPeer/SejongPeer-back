package com.sejong.sejongpeer.domain.member.dto.request;

import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SignUpRequest(
        @NotBlank(message = "계정은 비워둘 수 없습니다.") String account,
        @NotBlank(message = "비밀번호는 비워둘 수 없습니다.") String password,
        @NotBlank(message = "비밀번호 확인 칸은 비워둘 수 없습니다.") String passwordCheck,
        @NotBlank(message = "이름은 비워둘 수 없습니다.") String name,
        @NotBlank(message = "학번은 비워둘 수 없습니다.") String studentId,
        @NotBlank(message = "단과대학교는 비워둘 수 없습니다.") String college,
        @NotBlank(message = "전공은 비워둘 수 없습니다.") String major,
        String subCollege,
        String subMajor,
        @Positive(message = "학년은 비워둘 수 없습니다.") Integer grade,
        @NotNull(message = "성별은 비워둘 수 없습니다.") Gender gender,
        @NotBlank(message = "전화번호는 비워둘 수 없습니다.") String phoneNumber,
        @NotBlank(message = "닉네임은 비워둘 수 없습니다.") String nickname,
        @NotBlank(message = "카카오 계정은 비워둘 수 없습니다.") String kakaoAccount) {
    public boolean hasSubMajor() {
        if (subCollege != null && subMajor != null) {
            return true;
        }
        return false;
    }
}
