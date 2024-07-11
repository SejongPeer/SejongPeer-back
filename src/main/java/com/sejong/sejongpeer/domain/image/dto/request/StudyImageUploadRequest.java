package com.sejong.sejongpeer.domain.image.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record StudyImageUploadRequest(
	Long studyId,
	MultipartFile file
) { }
