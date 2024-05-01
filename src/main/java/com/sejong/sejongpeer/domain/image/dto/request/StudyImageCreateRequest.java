package com.sejong.sejongpeer.domain.image.dto.request;

import com.sejong.sejongpeer.domain.image.entity.type.ImageFileExtension;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StudyImageCreateRequest(
	@NotNull(message = "스터디 ID는 비워둘 수 없습니다.")
	@Schema(description = "스터디 ID", defaultValue = "1")
	Long studyId,
	@NotNull(message = "이미지 파일의 확장자는 비워둘 수 없습니다.")
	@Schema(description = "이미지 파일의 확장자", defaultValue = "JPEG")
	ImageFileExtension imageFileExtension
) {
}
