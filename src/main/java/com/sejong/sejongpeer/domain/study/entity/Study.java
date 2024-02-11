package com.sejong.sejongpeer.domain.study.entity;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("스터디 제목")
    @Column(length = 50, nullable = false)
    private String title;

    @Comment("스터디 내용")
    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Comment("모집 인원")
    private Integer recruitmentCount;

    @Comment("스터디 유형")
    @Enumerated(EnumType.STRING)
    private StudyType type;

    @Comment("스터디 모집 상태")
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus;

    @Comment("모집 시작 기간")
    private LocalDateTime recruitmentStartAt;

    @Comment("모집 마감 기간")
    private LocalDateTime recruitmentEndAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder(access = AccessLevel.PRIVATE)
    private Study(
            String title,
            String content,
            Integer recruitmentCount,
            StudyType type,
            RecruitmentStatus recruitmentStatus,
            LocalDateTime recruitmentStartAt,
            LocalDateTime recruitmentEndAt,
            Member member) {
        this.title = title;
        this.content = content;
        this.recruitmentCount = recruitmentCount;
        this.type = type;
        this.recruitmentStatus = recruitmentStatus;
        this.recruitmentStartAt = recruitmentStartAt;
        this.recruitmentEndAt = recruitmentEndAt;
        this.member = member;
    }

    public static Study createStudy(
            String title,
            String content,
            Integer recruitmentCount,
            StudyType type,
            LocalDateTime recruitmentStartAt,
            LocalDateTime recruitmentEndAt,
            Member member) {
        return Study.builder()
                .title(title)
                .content(content)
                .recruitmentCount(recruitmentCount)
                .type(type)
                .recruitmentStatus(RecruitmentStatus.RECRUITING)
                .recruitmentStartAt(recruitmentStartAt)
                .recruitmentEndAt(recruitmentEndAt)
                .member(member)
                .build();
    }

    public void updateStudy(
            String title,
            String content,
            Integer recruitmentCount,
            StudyType type,
            LocalDateTime recruitmentStartAt,
            LocalDateTime recruitmentEndAt) {
        this.title = title;
        this.content = content;
        this.recruitmentCount = recruitmentCount;
        this.type = type;
        this.recruitmentStartAt = recruitmentStartAt;
        this.recruitmentEndAt = recruitmentEndAt;
    }
}
