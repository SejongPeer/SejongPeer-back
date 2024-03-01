package com.sejong.sejongpeer.domain.honbab.entity.honbab.type;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum MenuCategoryOption {
	NO_MATTER("상관없음") {
		@Override
		public boolean isMatch(MenuCategoryOption candidateOption) {
			return true;
		}
	},
	SCHOOL("학식") {
		@Override
		public boolean isMatch(MenuCategoryOption candidateOption) {
			return candidateOption == NO_MATTER || candidateOption == SCHOOL;
		}
	},
	KOREAN("한식") {
		@Override
		public boolean isMatch(MenuCategoryOption candidateOption) {
			return candidateOption == NO_MATTER || candidateOption == KOREAN;
		}
	},
	CHINESE("중식") {
		@Override
		public boolean isMatch(MenuCategoryOption candidateOption) {
			return candidateOption == NO_MATTER || candidateOption == CHINESE;
		}
	},
	JAPANESE("일식") {
		@Override
		public boolean isMatch(MenuCategoryOption candidateOption) {
			return candidateOption == NO_MATTER || candidateOption == JAPANESE;
		}
	},
	WESTERN("양식") {
		@Override
		public boolean isMatch(MenuCategoryOption candidateOption) {
			return candidateOption == NO_MATTER || candidateOption == WESTERN;
		}
	};

	private final String value;

	public abstract boolean isMatch(MenuCategoryOption candidateOption);
}
