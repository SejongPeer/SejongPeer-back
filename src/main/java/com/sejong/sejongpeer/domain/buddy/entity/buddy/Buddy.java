package com.sejong.sejongpeer.domain.buddy.entity.buddy;

import org.hibernate.annotations.Comment;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.ClassTypeOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.CollegeMajorOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.GenderOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.GradeOption;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
	private ClassTypeOption classTypeOption;

	@Comment("버디 범위")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CollegeMajorOption collegeMajorOption;

	@Comment("버디 학년 범위")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private GradeOption gradeOption;

	@Comment("매칭 상태")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BuddyStatus status;

	@Comment("복수전공 확인")
	@Column(nullable = false)
	private boolean isSubMajor;

	@Builder(access = AccessLevel.PRIVATE)
	private Buddy(
		Member member,
		GenderOption genderOption,
		ClassTypeOption classTypeOption,
		CollegeMajorOption collegeMajorOption,
		GradeOption gradeOption,
		BuddyStatus status,
		boolean isSubMajor) {
		this.member = member;
		this.genderOption = genderOption;
		this.classTypeOption = classTypeOption;
		this.collegeMajorOption = collegeMajorOption;
		this.gradeOption = gradeOption;
		this.status = status;
		this.isSubMajor = isSubMajor;
	}

	public static Buddy createBuddy(
		Member member,
		GenderOption genderOption,
		ClassTypeOption classTypeOption,
		CollegeMajorOption collegeMajorOption,
		GradeOption gradeOption,
		BuddyStatus status,
		boolean isSubMajor) {
		return Buddy.builder()
			.member(member)
			.genderOption(genderOption)
			.classTypeOption(classTypeOption)
			.collegeMajorOption(collegeMajorOption)
			.gradeOption(gradeOption)
			.status(status)
			.isSubMajor(isSubMajor)
			.build();
	}

	public void changeStatus(BuddyStatus status) {
		this.status = status;
	}
}
