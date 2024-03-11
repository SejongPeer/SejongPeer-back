package com.sejong.sejongpeer.domain.honbab.dto.response;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.member.entity.Member;

public record MatchingPartnerInfoResponse(
	String collegeMajor,
	Integer grade,
	String name,
	String kakaoAccount,
	String menuCategoryOption
) {

	public static MatchingPartnerInfoResponse of(Member member, Honbab honbab) {
		return new MatchingPartnerInfoResponse(member.getCollegeMajor().getMajor(),
			member.getGrade(), member.getName(), member.getKakaoAccount(), honbab.getMenuCategoryOption().toString());
	}
}
