package com.sejong.sejongpeer.domain.study.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class StudyTagMap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	private Study study;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	private Tag tag;

	@Builder
	public StudyTagMap(Study study, Tag tag) {
		this.study = study;
		this.tag = tag;
	}
}
