package com.sejong.sejongpeer.domain.image.dto.request;


public record StudyImageUploadRequest(
	Long studyId,
	String base64Image
) { }
