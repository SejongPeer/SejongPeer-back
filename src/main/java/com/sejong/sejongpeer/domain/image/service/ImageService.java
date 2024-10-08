package com.sejong.sejongpeer.domain.image.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sejong.sejongpeer.domain.image.dto.request.StudyImageUploadRequest;
import com.sejong.sejongpeer.domain.image.dto.response.StudyImageUrlResponse;
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
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.global.common.constants.UrlConstants;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;
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
	private final StudyRepository studyRepository;
	private final MemberUtil memberUtil;

	// 스터디 이미지 Presigned Url 생성
	public PresignedUrlResponse createStudyPresignedUrl(final StudyImageCreateRequest request) {
		final Member member = memberUtil.getCurrentMember();
		Study study = findStudyById(request.studyId());

		validateStudyUserMismatch(study, member);

		String imageKey = generateUUID();
		String fileName =
			createFileName(
				ImageType.STUDY,
				request.studyId(),
				imageKey,
				request.imageFileExtension()
			);

		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			createGeneratePreSignedUrlRequest(
				s3Properties.bucket(),
				fileName,
				request.imageFileExtension().getUploadExtension()
			);

		String presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
		study.updateImageUploadStatusPending();

		imageRepository.save(
			Image.createImage(
				study,
				ImageType.STUDY,
				request.studyId(),
				imageKey,
				request.imageFileExtension()
			)
		);
		return PresignedUrlResponse.from(presignedUrl);
	}

	// 스터디 이미지 업로드
	public void uploadCompleteStudyImage(final StudyImageUploadCompleteRequest request) {
		final Member member = memberUtil.getCurrentMember();
		Study study = findStudyById(request.studyId());

		validateStudyUserMismatch(study, member);

		Image image = findImage(
			ImageType.STUDY,
			request.studyId(),
			request.imageFileExtension()
		);
		String imageUrl =
			createReadImageUrl(
				ImageType.STUDY,
				request.studyId(),
				image.getImageKey(),
				request.imageFileExtension()
			);
		study.updateImageUploadStatusComplete(imageUrl);
	}

	private Study findStudyById(final Long studyId) {
		return studyRepository
			.findById(studyId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
	}

	private void validateStudyUserMismatch(final Study study, final Member member) {
		if (!study.getMember().getId().equals(member.getId())) {
			throw new CustomException(ErrorCode.STUDY_USER_MISMATCH);
		}
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

	public List<StudyImageUrlResponse> uploadFiles(Long studyId, StudyImageUploadRequest request) throws IOException {
		List<Image> originalImages = imageRepository.findAllByStudyId(studyId);
		imageRepository.deleteAll(originalImages);

		List<String> base64ImagesList = request.base64ImagesList();

		return base64ImagesList.parallelStream()
			.map(base64Image -> {
				try {
					return uploadFile(studyId, base64Image);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			})
			.collect(Collectors.toUnmodifiableList());
	}

	public StudyImageUrlResponse uploadFile(Long studyId, String base64Image) throws IOException {
		String extension = "";
		Pattern pattern = Pattern.compile("^data:image/([a-zA-Z]+);base64,");
		Matcher matcher = pattern.matcher(base64Image);
		if (matcher.find()) {
			extension = matcher.group(1);
		}

		String base64ImageData = base64Image.replaceFirst("^data:image/[^;]+;base64,", "");
		byte[] imageBytes = Base64.getDecoder().decode(base64ImageData);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(imageBytes.length);
		metadata.setContentType("image/" + extension);

		String randomFileName = generateUUID();
		String fullFileName = randomFileName + "." + extension;

		amazonS3.putObject(s3Properties.bucket(), fullFileName, inputStream, metadata);
		String base64ToS3url = amazonS3.getUrl(s3Properties.bucket(), fullFileName).toExternalForm();

		Study study = findStudyById(studyId);
		Image savedImage = imageRepository.save(Image.createBase64ToImage(study, base64ToS3url));

		return new StudyImageUrlResponse(savedImage.getId(), base64ToS3url);
	}

}
