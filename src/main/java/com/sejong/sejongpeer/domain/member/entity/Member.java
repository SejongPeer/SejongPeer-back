package com.sejong.sejongpeer.domain.member.entity;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.entity.type.Status;
import com.sejong.sejongpeer.domain.study.entity.Study;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Member extends BaseAuditEntity {
    @Id
    @UuidGenerator
    @Column(name = "id", columnDefinition = "char(36)")
    private String id;

    @Column(columnDefinition = "varchar(20)", nullable = false, unique = true)
    private String account;

    @Column(columnDefinition = "varchar(70)", nullable = false)
    private String password;

    @Column(columnDefinition = "varchar(10)", nullable = false, unique = true)
    private String nickname;

    @Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
    private String kakaoAccount;

    @Column(columnDefinition = "varchar(40)", nullable = false)
    private String name;

    @Column(columnDefinition = "date", nullable = false)
    private LocalDate birthday;

    @Column(columnDefinition = "varchar(30)", nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('MALE', 'FEMALE')", nullable = false)
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    private CollegeMajor collegeMajor;

    @ManyToOne(fetch = FetchType.LAZY)
    private CollegeMajor collegeMinor; // 부전공 혹은 복수전공

    @Column(columnDefinition = "int", nullable = false)
    private Integer grade;

    @Column(columnDefinition = "varchar(10)", nullable = false)
    private String studentId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('ACTIVE', 'BLOCKED')", nullable = false)
    private Status status;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Study> studies = new ArrayList<>();

    @Builder
    private Member(
            String account,
            String password,
            String name,
            String nickname,
            LocalDate birthday,
            String phoneNumber,
            Gender gender,
            CollegeMajor collegeMajor,
            CollegeMajor collegeMinor,
            Integer grade,
            String studentId,
            String kakaoAccount) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.kakaoAccount = kakaoAccount;
        this.collegeMinor = collegeMinor;
        this.collegeMajor = collegeMajor;
        this.gender = gender;
        this.grade = grade;
        this.studentId = studentId;
        this.status = Status.ACTIVE;
    }

    public static Member create(
            SignUpRequest request,
            CollegeMajor collegeMajor,
            CollegeMajor collegeMinor,
            String encodedPassword) {
        return Member.builder()
                .name(request.name())
                .collegeMajor(collegeMajor)
                .collegeMinor(collegeMinor)
                .grade(request.grade())
                .phoneNumber(request.phoneNumber())
                .account(request.account())
                .password(encodedPassword)
                .birthday(request.birthday())
                .gender(request.gender())
                .studentId(request.studentId())
                .nickname(request.nickname())
                .kakaoAccount(request.kakaoAccount())
                .build();
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}
