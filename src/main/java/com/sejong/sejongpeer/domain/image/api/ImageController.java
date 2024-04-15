package com.sejong.sejongpeer.domain.image.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.image.dto.request.StudyImageCreateRequest;
import com.sejong.sejongpeer.domain.image.dto.request.StudyImageUploadCompleteRequest;
import com.sejong.sejongpeer.domain.image.dto.response.PresignedUrlResponse;
import com.sejong.sejongpeer.domain.image.service.ImageService;
import com.sejong.sejongpeer.security.util.SecurityContextUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "6. [이미지]", description = "이미지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {

	private final ImageService imageService;

	@Operation(
		summary = "스터디 이미지 Presigned URL 생성",
		description = "스터디 이미지 Presigned URL를 생성합니다.")
	@PostMapping("/study/upload-url")
	public PresignedUrlResponse createStudyPresignedUrl(
		@Valid @RequestBody StudyImageCreateRequest request
	) {
		String memberId = SecurityContextUtil.extractMemberId();
		return imageService.createStudyPresignedUrl(memberId, request);
	}

	@Operation(summary = "스터디 이미지 업로드 완료처리",
		description = "스터디 이미지 업로드 완료 시 호출하시면 됩니다.")
	@PostMapping("/study/upload-complete")
	public void uploadedStudyImage(
		@Valid @RequestBody StudyImageUploadCompleteRequest request) {
		String memberId = SecurityContextUtil.extractMemberId();
		imageService.uploadCompleteStudyImage(memberId, request);
	}
}
