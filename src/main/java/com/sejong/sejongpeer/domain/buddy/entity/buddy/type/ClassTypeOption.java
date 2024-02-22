package com.sejong.sejongpeer.domain.buddy.entity.buddy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassTypeOption {
	// "17".compareTo("18") : 음수 반환
	// candidate.compareTo(my) < 0 : candidate가 선배
	// candidate.compareTo(my) > 0 : candidate가 후배
	SENIOR("선배") {
		// me가 원하는 조건이 선배(senior)일 경우, candidate는 선배 학번이며, 원하는 조건은 상관없음 or 후배(junior)여야함
		@Override
		public boolean isMatch(
			String myStudentId, ClassTypeOption candidateOption, String candidateStudentId) {
			return candidateStudentId.compareTo(myStudentId) < 0
				&& (candidateOption == JUNIOR || candidateOption == NO_MATTER);
		}
	},
	JUNIOR("후배") {
		// me가 원하는 조건이 후배(junior)일 경우, candidate는 후배 학번이며, 원하는 조건은 상관없음 or 선배(senior)여야함
		@Override
		public boolean isMatch(
			String myStudentId, ClassTypeOption candidateOption, String candidateStudentId) {
			return candidateStudentId.compareTo(myStudentId) < 0
				&& (candidateOption == SENIOR || candidateOption == NO_MATTER);
		}
	},
	MATE("동기") {
		@Override
		public boolean isMatch(
			String myStudentId, ClassTypeOption candidateOption, String candidateStudentId) {
			return candidateStudentId.compareTo(myStudentId) == 0
				&& (candidateOption == MATE || candidateOption == NO_MATTER);
		}
	},

	NO_MATTER("상관없음") {
		// candidate가 선배를 원하면 candidate 자신은 후배가 되어야함
		@Override
		public boolean isMatch(
			String myStudentId, ClassTypeOption candidateOption, String candidateStudentId) {
			return candidateOption == NO_MATTER
				|| (candidateOption == SENIOR && candidateStudentId.compareTo(myStudentId) > 0)
				|| (candidateOption == JUNIOR && candidateStudentId.compareTo(myStudentId) < 0)
				|| (candidateOption == MATE
				&& myStudentId.compareTo(candidateStudentId) == 0);
		}
	};
	private final String value;

	public abstract boolean isMatch(
		String myStudentId, ClassTypeOption candidateType, String candidateStudentId);
}
