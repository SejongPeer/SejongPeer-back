package com.sejong.sejongpeer.domain.auth.dto.response;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;

public record SignInResponse(String accessToken,
							 String refreshToken,
							 String kakaoAccount,
							 String name,
							 String major,
							 String minor,
							 String nickname,
							 String phoneNumber,
							 String account,
							 String studentId,
							 Gender gender
) {
	public static SignInResponse of(String accessToken, String refreshToken, Member member) {
		return new SignInResponse(
			accessToken,
			refreshToken,
			member.getKakaoAccount(),
			member.getName(),
			member.getCollegeMajor().getMajor(),
			member.getCollegeMinor() != null ? member.getCollegeMinor().getMajor() : null,
			member.getNickname(),
			member.getPhoneNumber(),
			member.getAccount(),
			member.getStudentId(),
			member.getGender());
	}
}
