package com.sejong.sejongpeer.domain.member.dto.response;

import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;

public record MemberInfoResponse(
        String name,
        String kakaoAccount,
        String major,
        String minor,
        String nickname,
        String phoneNumber,
        String account,
        String studentId,
        Gender gender) {
    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
                member.getName(),
                member.getKakaoAccount(),
                member.getCollegeMajor().getMajor(),
                member.getCollegeMinor() != null ? member.getCollegeMinor().getMajor() : null,
                member.getNickname(),
                member.getPhoneNumber(),
                member.getAccount(),
                member.getStudentId(),
                member.getGender());
    }
}
