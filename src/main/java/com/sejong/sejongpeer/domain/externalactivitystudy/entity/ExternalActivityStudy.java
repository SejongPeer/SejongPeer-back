package com.sejong.sejongpeer.domain.externalactivitystudy.entity;

import com.sejong.sejongpeer.domain.common.BaseEntity;
import com.sejong.sejongpeer.domain.study.entity.Study;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalActivityStudy extends BaseEntity {
	@Id
	private Long studyId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "study_id")
	private Study externalActivityStudy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "external_activity_id")
	private ExternalActivity externalActivity;
}
