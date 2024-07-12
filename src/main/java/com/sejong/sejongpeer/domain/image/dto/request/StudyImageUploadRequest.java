package com.sejong.sejongpeer.domain.image.dto.request;


import java.util.List;

public record StudyImageUploadRequest(
	Long studyId,
	List<String> base64ImagesList
) { }
