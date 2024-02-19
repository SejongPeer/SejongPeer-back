package com.sejong.sejongpeer.domain.college.entity;

import com.sejong.sejongpeer.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("단과대 이름")
    @Column(length = 30, nullable = false)
    private String college;

    @Comment("학과 이름")
    @Column(length = 30, nullable = false)
    private String major;

    @OneToMany(mappedBy = "collegeMajor", cascade = CascadeType.ALL)
    private List<Member> majorMembers;

    @OneToMany(mappedBy = "collegeMinor", cascade = CascadeType.ALL)
    private List<Member> minorMembers;

    @Builder
    private CollegeMajor(String college, String major) {
        this.college = college;
        this.major = major;
    }
}
