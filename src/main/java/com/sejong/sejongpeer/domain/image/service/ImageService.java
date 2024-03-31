package com.sejong.sejongpeer.domain.image.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.sejong.sejongpeer.domain.image.dto.request.StudyImageCreateRequest;
import com.sejong.sejongpeer.domain.image.dto.request.StudyImageUploadCompleteRequest;
import com.sejong.sejongpeer.domain.image.dto.response.PresignedUrlResponse;
import com.sejong.sejongpeer.domain.image.entity.Image;
import com.sejong.sejongpeer.domain.image.entity.type.ImageFileExtension;
import com.sejong.sejongpeer.domain.image.entity.type.ImageType;
import com.sejong.sejongpeer.domain.image.repository.ImageRepository;
import com.sejong.sejongpeer.global.common.constants.UrlConstants;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.SpringEnvironmentUtil;
import com.sejong.sejongpeer.infra.config.properties.S3Properties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

	private final SpringEnvironmentUtil springEnvironmentUtil;
	private final S3Properties s3Properties;
	private final AmazonS3 amazonS3;
	private final ImageRepository imageRepository;

	// 스터디 이미지 Presigned Url 생성
	public PresignedUrlResponse createStudyPresignedUrl(StudyImageCreateRequest request) {
		return null;
	}

	// 스터디 이미지 업로드
	public void uploadCompleteStudyImage(StudyImageUploadCompleteRequest request) {

	}

	private GeneratePresignedUrlRequest createGeneratePreSignedUrlRequest(
		String bucket, String fileName, String fileExtension) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			new GeneratePresignedUrlRequest(bucket, fileName, HttpMethod.PUT)
				.withKey(fileName)
				.withContentType("image/" + fileExtension)
				.withExpiration(getPreSignedUrlExpiration());

		generatePresignedUrlRequest.addRequestParameter(
			Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

		return generatePresignedUrlRequest;
	}

	private Image findImage(
		ImageType imageType, Long targetId, ImageFileExtension imageFileExtension) {
		return imageRepository
			.queryImageKey(imageType, targetId, imageFileExtension)
			.orElseThrow(() -> new CustomException(ErrorCode.IMAGE_KEY_NOT_FOUND));
	}

	private String generateUUID() {
		return UUID.randomUUID().toString();
	}

	private String createFileName(
		ImageType imageType,
		Long targetId,
		String imageKey,
		ImageFileExtension imageFileExtension) {
		return springEnvironmentUtil.getCurrentProfile()
			+ "/"
			+ imageType.getValue()
			+ "/"
			+ targetId
			+ "/"
			+ imageKey
			+ "."
			+ imageFileExtension.getUploadExtension();
	}

	private String createReadImageUrl(
		ImageType imageType,
		Long targetId,
		String imageKey,
		ImageFileExtension imageFileExtension) {
		return UrlConstants.IMAGE_DOMAIN_URL.getValue()
			+ "/"
			+ springEnvironmentUtil.getCurrentProfile()
			+ "/"
			+ imageType.getValue()
			+ "/"
			+ targetId
			+ "/"
			+ imageKey
			+ "."
			+ imageFileExtension.getUploadExtension();
	}

	private Date getPreSignedUrlExpiration() {
		Date expiration = new Date();
		var expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * 30;
		expiration.setTime(expTimeMillis);
		return expiration;
	}

}
