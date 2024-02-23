package com.sejong.sejongpeer.domain.buddy.entity.buddy.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GradeOption {
    NO_MATTER("상관없음", 0) {
        @Override
        public boolean isMatch(int myGrade, GradeOption candidateDesireOption, int candidateGrade) {
            return candidateDesireOption == NO_MATTER
                    || candidateDesireOption.getGrade() == myGrade;
        }
    },
    GRADE_1("1학년", 1) {
        @Override
        public boolean isMatch(int myGrade, GradeOption candidateDesireOption, int candidateGrade) {
            return candidateGrade == 1
                    && (candidateDesireOption == NO_MATTER
                            || candidateDesireOption.getGrade() == myGrade);
        }
    },
    GRADE_2("2학년", 2) {
        @Override
        public boolean isMatch(int myGrade, GradeOption candidateDesireOption, int candidateGrade) {
            return candidateGrade == 2
                    && (candidateDesireOption == NO_MATTER
                            || candidateDesireOption.getGrade() == myGrade);
        }
    },
    GRADE_3("3학년", 3) {
        @Override
        public boolean isMatch(int myGrade, GradeOption candidateDesireOption, int candidateGrade) {
            return candidateGrade == 3
                    && (candidateDesireOption == NO_MATTER
                            || candidateDesireOption.getGrade() == myGrade);
        }
    },
    GRADE_4("4학년", 4) {
        @Override
        public boolean isMatch(int myGrade, GradeOption candidateDesireOption, int candidateGrade) {
            return candidateGrade == 4
                    && (candidateDesireOption == NO_MATTER
                            || candidateDesireOption.getGrade() == myGrade);
        }
    };

    private String value;
    private int grade;

    /**
     * @param myGrade 나의 실제 학년
     * @param candidateGradeOption 상대방이 원하는 학년
     * @param candidateGrade 상대방의 실제 학년
     * @return 나의 실제학년 == 상대방이 원하는 학년, 내가 원하는 학년 == 상대방의 실제 학년이어야 함. '상관없음'의 경우 다른 쪽이 '상관없음' 이거나 실제
     *     학년 == 상대방이 원하는 학년인지 체크.
     */
    public abstract boolean isMatch(
            int myGrade, GradeOption candidateGradeOption, int candidateGrade);
}
