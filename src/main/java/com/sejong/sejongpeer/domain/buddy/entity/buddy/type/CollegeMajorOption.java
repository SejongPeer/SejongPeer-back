package com.sejong.sejongpeer.domain.buddy.entity.buddy.type;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CollegeMajorOption {
	SAME_COLLEGE("같은 단과대") {
		// me가 동일 단과대를 원할 경우, candidate는 me와 같은 단과대여야하며 선택 범위는 상관없음 혹은 동일 단과대
		@Override
		public boolean isMatch(
			CollegeMajor myCollegeMajor,
			CollegeMajorOption candidateOption,
			CollegeMajor candidateCollegeMajor) {
			return myCollegeMajor.getCollege().equals(candidateCollegeMajor.getCollege())
				&& (candidateOption == SAME_COLLEGE || candidateOption == NO_MATTER);
		}
	},
	// me가 동일 학과를 원할 경우, candidate는 me와 같은 학과여야하며 선택 범위는 상관없음 혹은 동일 학과
	SAME_DEPARTMENT("같은 학과") {
		@Override
		public boolean isMatch(
			CollegeMajor myCollegeMajor,
			CollegeMajorOption candidateOption,
			CollegeMajor candidateCollegeMajor) {
			return myCollegeMajor.getMajor().equals(candidateCollegeMajor.getMajor())
				&& (candidateOption == SAME_DEPARTMENT || candidateOption == NO_MATTER);
		}
	},
	NO_MATTER("상관없음") {
		// me가 상관없음일 경우, candidate가 me와 짝이 될 수 있는지만 검증
		@Override
		public boolean isMatch(
			CollegeMajor myCollegeMajor,
			CollegeMajorOption candidateOption,
			CollegeMajor candidateCollegeMajor) {
			return candidateOption == NO_MATTER
				|| (candidateOption == SAME_COLLEGE
				&& myCollegeMajor
				.getCollege()
				.equals(candidateCollegeMajor.getCollege()))
				|| (candidateOption == SAME_DEPARTMENT
				&& myCollegeMajor.equals(candidateCollegeMajor));
		}
	};

	private final String value;

	public abstract boolean isMatch(
		CollegeMajor myCollegeMajor,
		CollegeMajorOption candidateOption,
		CollegeMajor candidateCollegeMajor);
}
