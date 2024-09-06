package com.sejong.sejongpeer.domain.member.dto.request;

import com.sejong.sejongpeer.domain.member.entity.type.Gender;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record SignUpRequest(
	@NotBlank(message = "계정은 비워둘 수 없습니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]{4,24}$", message = "최소 4자, 최대 24자 영문과 숫자로만 이루어져야합니다.") String account,
	@NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
	@Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*?]{10,}$", message = "비밀번호는 최소 10자 이상의 영문, 숫자 및 특수문자(!@#$%^&*?)로만 이루어져야 합니다.") String password,
	@NotBlank(message = "비밀번호 확인 칸은 비워둘 수 없습니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]{10,}$") String passwordCheck,
	@NotBlank(message = "이름은 비워둘 수 없습니다.") String name,
	@NotBlank(message = "학번은 비워둘 수 없습니다.") String studentId,
	@NotBlank(message = "단과대학교는 비워둘 수 없습니다.") String college,
	@NotBlank(message = "전공은 비워둘 수 없습니다.") String major,
	String subCollege,
	String subMajor,
	@Positive(message = "학년은 비워둘 수 없습니다.") Integer grade,
	@NotNull(message = "성별은 비워둘 수 없습니다.") Gender gender,
	@NotBlank(message = "전화번호는 비워둘 수 없습니다.")
	@Pattern(regexp = "^010[0-9]{8}$", message = "휴대폰 번호를 정확히 입력해주세요.") String phoneNumber,
	@NotBlank(message = "닉네임은 비워둘 수 없습니다.")
	@Pattern(regexp = "^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9]{2,8}$", message = "닉네임은 2자 이상 8자 이하 한글, 영어, 숫자만 입력해주세요.") String nickname,
	@NotBlank(message = "카카오 계정은 비워둘 수 없습니다.") String kakaoAccount) {
	public boolean hasSubMajor() {
		if (subCollege != null && subMajor != null) {
			return true;
		}
		return false;
	}
}
