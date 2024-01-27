package com.sejong.sejongpeer.domain.member.entity;

import java.time.LocalDate;

import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.dto.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.entity.type.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class Member extends BaseAuditEntity {
	@Id
	@UuidGenerator
	@Column(name = "id", columnDefinition = "char(36)")
	private String id;

	@Column(columnDefinition = "varchar(20)", nullable = false, unique = true)
	private String account;

	@Column(columnDefinition = "varchar(70)", nullable = false)
	private String password;

	@Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
	private String email;

	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String name;

	@Column(columnDefinition = "date", nullable = false)
	private LocalDate birthday;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('MALE', 'FEMALE')", nullable = false)
	private Gender gender;

	@Column(columnDefinition = "varchar(20)", nullable = false)
	private String major;

	@Column(columnDefinition = "int", nullable = false)
	private Integer grade;

	@Column(columnDefinition = "varchar(10)", nullable = false)
	private String studentId;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('ACTIVE', 'BLOCKED')", nullable = false)
	private Status status;

	@Builder
	private Member(
		String account,
		String password,
		String email,
		String name,
		LocalDate birthday,
		String phoneNumber,
		Gender gender,
		String major,
		Integer grade,
		String studentId) {
		this.account = account;
		this.password = password;
		this.email = email;
		this.name = name;
		this.birthday = birthday;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.major = major;
		this.grade = grade;
		this.studentId = studentId;
		this.status = Status.ACTIVE;
	}

	public static Member create(SignUpRequest request, String encodedPassword) {
		return Member.builder()
			.name(request.name())
			.major(request.major())
			.grade(request.grade())
			.phoneNumber(request.phoneNumber())
			.email(request.email())
			.account(request.account())
			.password(encodedPassword)
			.birthday(request.birthday())
			.gender(request.gender())
			.studentId(request.studentId())
			.build();
	}
}
