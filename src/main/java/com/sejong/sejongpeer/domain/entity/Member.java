package com.sejong.sejongpeer.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.sejong.sejongpeer.domain.entity.type.Gender;
import com.sejong.sejongpeer.domain.entity.type.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2")
	@Column(columnDefinition = "char(36)")
	private String id;

	@Column(columnDefinition = "varchar(20)", nullable = false, unique = true)
	private String account;

	@Column(columnDefinition = "varchar(70)", nullable = false)
	private String password;

	@Column(columnDefinition = "varchar(50)", nullable = false, unique = true)
	@Pattern(regexp = "^[a-zA-z0-9]+@sju\\.ac\\.kr$")
	private String email;

	@Column(columnDefinition = "varchar(40)", nullable = false)
	private String name;

	@Column(columnDefinition = "date", nullable = false)
	private LocalDate birthday;

	@Column(columnDefinition = "varchar(30)", nullable = false)
	@Pattern(regexp = "^010\\d{4}\\d{4}$")
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

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(updatable = false)
	private LocalDateTime updatedAt;
}

