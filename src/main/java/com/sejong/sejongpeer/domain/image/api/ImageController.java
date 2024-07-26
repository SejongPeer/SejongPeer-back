package com.sejong.sejongpeer.domain.image.api;

import com.sejong.sejongpeer.domain.image.dto.request.StudyImageUploadRequest;
import com.sejong.sejongpeer.domain.image.dto.response.StudyImageUrlResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import com.sejong.sejongpeer.domain.image.dto.request.StudyImageCreateRequest;
import com.sejong.sejongpeer.domain.image.dto.request.StudyImageUploadCompleteRequest;
import com.sejong.sejongpeer.domain.image.dto.response.PresignedUrlResponse;
import com.sejong.sejongpeer.domain.image.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "6. [이미지]", description = "이미지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/image")
public class ImageController {
	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	private final ImageService imageService;

//	@Operation(
//		summary = "스터디 이미지 Presigned URL 생성",
//		description = "스터디 이미지 Presigned URL를 생성합니다.")
//	@PostMapping("/study/upload-url")
//	public PresignedUrlResponse createStudyPresignedUrl(
//		@Valid @RequestBody StudyImageCreateRequest request
//	) {
//		return imageService.createStudyPresignedUrl(request);
//	}
//
//	@Operation(summary = "스터디 이미지 업로드 완료처리",
//		description = "스터디 이미지 업로드 완료 시 호출하시면 됩니다.")
//	@PostMapping("/study/upload-complete")
//	public void uploadedStudyImage(
//		@Valid @RequestBody StudyImageUploadCompleteRequest request) {
//		imageService.uploadCompleteStudyImage(request);
//	}

	@Operation(
		summary = "스터디 게시글 별 이미지 등록 및 수정",
		description = "스터디 이미지를 클라우드에 업로드하여 이미지 경로를 반환합니다.")
	@PostMapping("/study/upload")
	public List<StudyImageUrlResponse> uploadStudyImage(@RequestBody StudyImageUploadRequest request) throws IOException {
		return imageService.uploadFiles(request.studyId(), request);
	}
}
