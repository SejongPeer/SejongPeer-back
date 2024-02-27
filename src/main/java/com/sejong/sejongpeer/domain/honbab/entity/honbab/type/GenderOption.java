package com.sejong.sejongpeer.domain.honbab.entity.honbab.type;

import com.sejong.sejongpeer.domain.member.entity.type.Gender;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum GenderOption {
	SAME("동일한 성별") {
		// me가 동성을 원할 경우, candidate 성별 선택과 상관없이 동성이기만 하면 됨
		// 선택지가 '상관없음', '동성'만있기에 한 쪽이 동성을 원하는 경우, 다른 한 쪽의 성별 선택은 의미 없음
		@Override
		public boolean isMatch(
			Gender myGender, GenderOption candidateOption, Gender candidateGender) {
			return myGender == candidateGender;
		}
	},
	NO_MATTER("상관없음") {
		// me가 상관없을 경우, candidate가 동성만 원하는 경우가 있을 수 있음
		@Override
		public boolean isMatch(
			Gender myGender, GenderOption candidateOption, Gender candidateGender) {
			return candidateOption == NO_MATTER
				|| (candidateOption == SAME && myGender == candidateGender);
		}
	};
	private final String value;

	public abstract boolean isMatch(Gender myGender, GenderOption candidateOption, Gender candidateGender);
}
