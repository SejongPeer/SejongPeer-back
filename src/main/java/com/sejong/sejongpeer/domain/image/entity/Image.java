package com.sejong.sejongpeer.domain.image.entity;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.image.entity.type.ImageFileExtension;
import com.sejong.sejongpeer.domain.image.entity.type.ImageType;

import com.sejong.sejongpeer.domain.study.entity.Study;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseAuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "image_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	private ImageType imageType;

	private Long targetId;

	@Column(length = 36)
	private String imageKey;

	@Enumerated(EnumType.STRING)
	private ImageFileExtension imageFileExtension;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "study_id")
	private Study study;

	@Builder(access = AccessLevel.PRIVATE)
	private Image(
		Long id,
		Study study,
		ImageType imageType,
		Long targetId,
		String imageKey,
		ImageFileExtension imageFileExtension) {
		this.id = id;
		this.study = study;
		this.imageType = imageType;
		this.targetId = targetId;
		this.imageKey = imageKey;
		this.imageFileExtension = imageFileExtension;
	}

	public static Image createImage(
		Study study,
		ImageType imageType,
		Long targetId,
		String imageKey,
		ImageFileExtension imageFileExtension) {
		return Image.builder()
			.study(study)
			.imageType(imageType)
			.targetId(targetId)
			.imageKey(imageKey)
			.imageFileExtension(imageFileExtension)
			.build();
	}
}
