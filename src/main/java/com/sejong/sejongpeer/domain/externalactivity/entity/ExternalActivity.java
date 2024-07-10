package com.sejong.sejongpeer.domain.externalactivity.entity;

import java.util.ArrayList;
import java.util.List;

import com.sejong.sejongpeer.domain.common.BaseEntity;
import com.sejong.sejongpeer.domain.study.entity.ExternalActivityStudy;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExternalActivity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String description;

	@OneToMany(mappedBy = "externalActivity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ExternalActivityStudy> externalActivityStudies = new ArrayList<>();
}
