package com.sejong.sejongpeer.domain.scrap.entity;

import org.hibernate.annotations.Comment;

import com.sejong.sejongpeer.domain.common.BaseEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;

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

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private ScrapType type;

	@Comment("스크랩한 사용자")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	private Study study;

	@Builder(access = AccessLevel.PRIVATE)
	private Scrap(ScrapType type, Member member, Study study) {
		this.type = type;
		this.member = member;
		this.study = study;
	}

	public static Scrap createScrap(ScrapType type, Member member, Study study) {
		return Scrap.builder()
			.type(type)
			.member(member)
			.study(study)
			.build();
	}
}
