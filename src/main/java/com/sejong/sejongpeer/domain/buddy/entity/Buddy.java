package com.sejong.sejongpeer.domain.buddy.entity;

import com.sejong.sejongpeer.domain.buddy.entity.type.*;
import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import jakarta.persistence.*;
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
	@JoinColumn(name = "id")
	private Member member;

	@Comment("버디 성별")
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('SAME', 'NONE')", nullable = false)
	private GenderOption genderOption;

	@Comment("버디 타입")
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('SENIOR', 'JUNIOR', 'MATE', 'NONE')", nullable = false)
	private BuddyType type;

	@Comment("버디 범위")
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('COLLEGE', 'DEPARTMENT', 'SAME_COLLEGE', 'SAME_DEPARTMENT', 'NONE')", nullable = false)
	private BuddyRange range;

	@Comment("버디 학년 범위")
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('GRADE_1', 'GRADE_2', 'GRADE_3', 'GRADE_4', 'NONE')", nullable = false)
	private GradeOption gradeOption;

	@Comment("매칭 상태")
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "enum('IN_PROGRESS', 'CANCEL', 'ACCEPT','REJECT', 'DENIED', 'MATCHING_COMPLETED', 'FOUND_BUDDY')", nullable = false)
	private MatchingStatus matchingStatus;

	@OneToOne(mappedBy = "buddy")
	private BuddyMatched buddyMatched;


	@Builder
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
