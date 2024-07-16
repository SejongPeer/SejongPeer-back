package com.sejong.sejongpeer.domain.scrap.dto.response;

public record StudyScrapCountResponse(
	int scrapCount
) {

	public static StudyScrapCountResponse of(int scrapCount) {
		return new StudyScrapCountResponse(scrapCount);
	}
}
