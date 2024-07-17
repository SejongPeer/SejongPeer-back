package com.sejong.sejongpeer.domain.studyrelation.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.studyrelation.entity.type.StudyMatchingStatus;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRelation extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	private LocalDateTime canceledAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	private Study study;

	@Comment("스터디 신청 현황")
	@Enumerated(EnumType.STRING)
	private StudyMatchingStatus status;

	@Builder(access = AccessLevel.PRIVATE)
	private StudyRelation(Member member, Study study, StudyMatchingStatus status) {
		this.member = member;
		this.study = study;
		this.status = status;
	}

	public static StudyRelation createStudyRelations(Member member, Study study) {
		return StudyRelation.builder()
			.member(member)
			.study(study)
			.status(StudyMatchingStatus.PENDING)
			.build();
	}

	public void changeStudyMatchingStatus(StudyMatchingStatus status) {
		this.status = status;
	}

	public void registerCanceledAt(LocalDateTime localDateTime) {
		this.canceledAt = localDateTime;
	}
}
