package com.sejong.sejongpeer.domain.honbab.util;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.GenderOption;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.MenuCategoryOption;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;

public class HonbabFilter {
	public static boolean filterSutiableGender(Honbab candidate, Honbab me) {
		Gender myGender = me.getMember().getGender();
		Gender candidateGender = candidate.getMember().getGender();

		GenderOption myOption = me.getGenderOption();
		GenderOption candidateOption = candidate.getGenderOption();

		return myOption.isMatch(myGender, candidateOption, candidateGender);
	}

	public static boolean filterSuitableMenuCategory(Honbab candidate, Honbab me) {
		MenuCategoryOption myOption = me.getMenuCategoryOption();
		MenuCategoryOption candidateOption = candidate.getMenuCategoryOption();

		return myOption.isMatch(candidateOption);
	}
}
