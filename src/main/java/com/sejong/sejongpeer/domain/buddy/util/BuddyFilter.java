package com.sejong.sejongpeer.domain.buddy.util;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.ClassTypeOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.CollegeMajorOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.GenderOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.GradeOption;
import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;

public final class BuddyFilter {
	public static boolean filterSuitableCollegeMajor(Buddy candidate, Buddy me) {
		CollegeMajorOption myBuddyRange = me.getCollegeMajorOption();
		CollegeMajorOption candidateBuddyRange = candidate.getCollegeMajorOption();

		// 복전 및 부전공을 선택한 경우, 복전 및 부전공을 선택한 경우에 따라 검색할 전공이 달라짐
		CollegeMajor myCollegeMajor =
			me.isSubMajor()
				? me.getMember().getCollegeMinor()
				: me.getMember().getCollegeMajor();
		CollegeMajor candidateCollegeMajor =
			candidate.isSubMajor()
				? candidate.getMember().getCollegeMinor()
				: candidate.getMember().getCollegeMajor();

		return myBuddyRange.isMatch(myCollegeMajor, candidateBuddyRange, candidateCollegeMajor);
	}

	public static boolean filterSuitableType(Buddy candidate, Buddy me) {
		ClassTypeOption myType = me.getClassTypeOption();
		ClassTypeOption candidateType = candidate.getClassTypeOption();

		// 학번이 더 작은 경우가 선배임을 유의
		String myStudentId = me.getMember().getStudentId().substring(0, 2);
		String candidateStudentId = candidate.getMember().getStudentId().substring(0, 2);

		return myType.isMatch(myStudentId, candidateType, candidateStudentId);
	}

	public static boolean filterSuitableGrade(Buddy candidate, Buddy me) {
		GradeOption myGradeOption = me.getGradeOption();
		GradeOption candidateGradeOption = candidate.getGradeOption();

		int myGrade = me.getMember().getGrade();
		int candidateGrade = candidate.getMember().getGrade();

		return myGradeOption.isMatch(myGrade, candidateGradeOption, candidateGrade);
	}

	public static boolean filterSuitableGender(Buddy candidate, Buddy me) {
		GenderOption myOption = me.getGenderOption();
		GenderOption candidateOption = candidate.getGenderOption();

		Gender myGender = me.getMember().getGender();
		Gender candidateGender = candidate.getMember().getGender();

		return myOption.isMatch(myGender, candidateOption, candidateGender);
	}
}
