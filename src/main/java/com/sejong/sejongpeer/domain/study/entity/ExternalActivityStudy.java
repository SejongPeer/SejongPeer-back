package com.sejong.sejongpeer.domain.study.entity;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.externalactivity.entity.ExternalActivity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalActivityStudy extends BaseAuditEntity {
	@Id
	private Long studyId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Study study;

	@ManyToOne(fetch = FetchType.LAZY)
	private ExternalActivity externalActivity;

	@Builder(access = AccessLevel.PRIVATE)
	private ExternalActivityStudy(Study study, ExternalActivity externalActivity) {
		this.study = study;
		this.externalActivity = externalActivity;
	}

	public static ExternalActivityStudy create(ExternalActivity externalActivity, Study study) {
		return ExternalActivityStudy.builder()
			.externalActivity(externalActivity)
			.study(study)
			.build();
	}
}
