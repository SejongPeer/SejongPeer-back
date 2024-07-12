package com.sejong.sejongpeer.domain.image.dto.response;

import com.sejong.sejongpeer.domain.image.entity.Image;

public record StudyImageUrlResponse(
	Long imageId,
	String imgUrl
) {
	public static StudyImageUrlResponse fromImage(Image image) {
		return new StudyImageUrlResponse(
			image.getId(),
			image.getImgUrl()
		);
	}
}
