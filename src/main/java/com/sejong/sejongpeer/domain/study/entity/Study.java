package com.sejong.sejongpeer.domain.study.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.dto.request.LectureStudyCreateRequest;
import com.sejong.sejongpeer.domain.study.entity.type.ImageUploadStatus;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Comment("스터디 제목")
	@Column(length = 50, nullable = false)
	private String title;

	@Comment("스터디 내용")
	@Column(columnDefinition = "text", nullable = false)
	private String content;

	@Comment("모집 인원")
	private Integer recruitmentCount;

	@Comment("스터디 유형")
	@Enumerated(EnumType.STRING)
	private StudyType type;

	@Comment("스터디 모집 상태")
	@Enumerated(EnumType.STRING)
	private RecruitmentStatus recruitmentStatus;

	@Comment("스터디 이미지")
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	private ImageUploadStatus uploadStatus;

	@Comment("모집 시작 기간")
	private LocalDateTime recruitmentStartAt;

	@Comment("모집 마감 기간")
	private LocalDateTime recruitmentEndAt;

	@Comment("오픈카카오톡 링크")
	private String kakaoLink;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Builder(access = AccessLevel.PRIVATE)
	private Study(
		String title,
		String content,
		Integer recruitmentCount,
		StudyType type,
		RecruitmentStatus recruitmentStatus,
		String imageUrl,
		ImageUploadStatus uploadStatus,
		LocalDateTime recruitmentStartAt,
		LocalDateTime recruitmentEndAt,
		String kakaoLink,
		Member member) {
		this.title = title;
		this.content = content;
		this.recruitmentCount = recruitmentCount;
		this.type = type;
		this.uploadStatus = uploadStatus;
		this.recruitmentStatus = recruitmentStatus;
		this.imageUrl = imageUrl;
		this.recruitmentStartAt = recruitmentStartAt;
		this.recruitmentEndAt = recruitmentEndAt;
		this.kakaoLink = kakaoLink;
		this.member = member;
	}

	public static Study createLecutreStudy(Member member, LectureStudyCreateRequest request) {
		return Study.builder()
			.title(request.title())
			.content(request.content())
			.recruitmentCount(request.recruitmentCount())
			.type(StudyType.LECTURE)
			.uploadStatus(ImageUploadStatus.NONE)
			.recruitmentStatus(RecruitmentStatus.RECRUITING)
			.recruitmentStartAt(request.recruitmentStartAt())
			.recruitmentEndAt(request.recruitmentEndAt())
			.member(member)
			.build();
	}

	public void updateStudy(
		String title,
		String content,
		Integer recruitmentCount,
		StudyType type,
		LocalDateTime recruitmentStartAt,
		LocalDateTime recruitmentEndAt) {
		this.title = title;
		this.content = content;
		this.recruitmentCount = recruitmentCount;
		this.type = type;
		this.recruitmentStartAt = recruitmentStartAt;
		this.recruitmentEndAt = recruitmentEndAt;
	}

	public void updateImageUploadStatusPending() {
		if (this.uploadStatus != ImageUploadStatus.NONE) {
			throw new CustomException(ErrorCode.STUDY_UPLOAD_STATUS_IS_NOT_NONE);
		}
		this.uploadStatus = ImageUploadStatus.PENDING;
	}

	public void updateImageUploadStatusComplete(String imageUrl) {
		if (this.uploadStatus != ImageUploadStatus.PENDING) {
			throw new CustomException(ErrorCode.STUDY_UPLOAD_STATUS_IS_NOT_PENDING);
		}
		this.uploadStatus = ImageUploadStatus.COMPLETE;
		this.imageUrl = imageUrl;
	}
}
