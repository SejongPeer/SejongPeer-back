package com.sejong.sejongpeer.domain.college.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "college-major-relations")
public class CollegeMajor {

    @Id @GeneratedValue private Long id;

    @Comment("단과대 이름")
    @Column(length = 30, nullable = false)
    private String college;

    @Comment("학과 이름")
    @Column(length = 30, nullable = false)
    private String major;

    @Builder
    private CollegeMajor(String college, String major) {
        this.college = college;
        this.major = major;
    }
}
