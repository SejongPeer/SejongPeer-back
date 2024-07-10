package com.sejong.sejongpeer.domain.externalactivity.dto;

import com.sejong.sejongpeer.domain.externalactivity.entity.ExternalActivity;

public record ExternalActivityCategoryResponse(
	Long id,
	String categoryName,
	String categoryDescription
) {

	public static ExternalActivityCategoryResponse from(ExternalActivity externalActivity) {
		return new ExternalActivityCategoryResponse(
			externalActivity.getId(),
			externalActivity.getName(),
			externalActivity.getDescription()
		);
	}
}
