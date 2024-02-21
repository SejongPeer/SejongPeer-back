package com.sejong.sejongpeer.domain.buddy.entity;

import com.sejong.sejongpeer.domain.buddy.entity.type.*;
import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Buddy extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Comment("버디 성별")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenderOption genderOption;

    @Comment("버디 타입")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuddyType type;

    @Comment("버디 범위")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuddyRange range;

    @Comment("버디 학년 범위")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GradeOption gradeOption;

    @Comment("매칭 상태")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchingStatus matchingStatus;

	@Comment("복수전공 확인")
	@Column(nullable = false)
	private boolean isSubMajor;

    @Builder(access = AccessLevel.PRIVATE)
    private Buddy(
            Member member,
            GenderOption genderOption,
            BuddyType type,
            BuddyRange range,
            GradeOption gradeOption,
            MatchingStatus matchingStatus) {
        this.member = member;
        this.genderOption = genderOption;
        this.type = type;
        this.range = range;
        this.gradeOption = gradeOption;
        this.matchingStatus = matchingStatus;
    }

    public static Buddy createBuddy(
            Member member,
            GenderOption genderOption,
            BuddyType type,
            BuddyRange range,
            GradeOption gradeOption,
            MatchingStatus matchingStatus) {
        return Buddy.builder()
                .member(member)
                .genderOption(genderOption)
                .type(type)
                .range(range)
                .gradeOption(gradeOption)
                .matchingStatus(matchingStatus)
                .build();
    }
}
