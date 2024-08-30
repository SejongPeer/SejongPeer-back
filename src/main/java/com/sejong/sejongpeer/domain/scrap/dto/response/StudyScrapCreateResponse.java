package com.sejong.sejongpeer.domain.scrap.dto.response;

import com.sejong.sejongpeer.domain.scrap.entity.Scrap;

public record StudyScrapCreateResponse(
	Long scrapId
) {
	public static StudyScrapCreateResponse from(Scrap scrap) {
		return new StudyScrapCreateResponse(scrap.getId());
	}
}
