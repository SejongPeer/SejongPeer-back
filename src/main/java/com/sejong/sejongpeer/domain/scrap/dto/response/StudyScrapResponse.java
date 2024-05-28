package com.sejong.sejongpeer.domain.scrap.dto.response;

public record StudyScrapResponse(
	Boolean isScrap,
	int scarpCount
) {
	public static StudyScrapResponse of(Boolean isScrap, int scarpCount) {
		return new StudyScrapResponse(isScrap, scarpCount);
	}
}
