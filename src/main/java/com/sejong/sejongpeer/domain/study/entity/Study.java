package com.sejong.sejongpeer.domain.study.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sejong.sejongpeer.domain.image.entity.Image;
import com.sejong.sejongpeer.domain.study.entity.type.Frequency;
import com.sejong.sejongpeer.domain.study.entity.type.StudyMethod;
import com.sejong.sejongpeer.domain.studyrelation.entity.StudyRelation;
import org.hibernate.annotations.Comment;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.scrap.entity.Scrap;
import com.sejong.sejongpeer.domain.study.entity.type.ImageUploadStatus;
import com.sejong.sejongpeer.domain.study.entity.type.RecruitmentStatus;
import com.sejong.sejongpeer.domain.study.entity.type.StudyType;
import com.sejong.sejongpeer.domain.study.vo.StudyVo;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
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

	@Comment("참여 인원")
	private Integer participantsCount;

	@Comment("스터디 유형")
	@Enumerated(EnumType.STRING)
	private StudyType type;

	@Comment("스터디 모집 상태")
	@Enumerated(EnumType.STRING)
	private RecruitmentStatus recruitmentStatus;

	@Comment("스터디 이미지")
	private String imageUrl;

	@Comment("오픈카카오톡 링크")
	@Column(length = 100, nullable = false)
	private String kakaoLink;

	@Comment("질문 링크")
	@Column(length = 100, nullable = false)
	private String questionLink;

	@Comment("스터디 방식")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StudyMethod method;

	@Comment("모임 빈도")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Frequency frequency;

	@Enumerated(EnumType.STRING)
	private ImageUploadStatus uploadStatus;

	@Comment("모집 시작 기간")
	private LocalDateTime recruitmentStartAt;

	@Comment("모집 마감 기간")
	private LocalDateTime recruitmentEndAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Scrap> scraps = new ArrayList<>();

	@OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StudyRelation> studyRelations = new ArrayList<>();

	@OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<StudyTagMap> studyTagMaps = new HashSet<>();

	@OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();

	@Builder(access = AccessLevel.PRIVATE)
	private Study(
		String title,
		String content,
		Integer recruitmentCount,
		StudyType type,
		String imageUrl,
		String kakaoLink,
		String questionLink,
		StudyMethod method,
		Frequency frequency,
		LocalDateTime recruitmentStartAt,
		LocalDateTime recruitmentEndAt,
		Member member) {
		this.title = title;
		this.content = content;
		this.recruitmentCount = recruitmentCount;
		this.type = type;
		this.participantsCount = 0; // 상태값 초기화
		this.uploadStatus = ImageUploadStatus.NONE; // 상태값 초기화
		this.recruitmentStatus = RecruitmentStatus.RECRUITING; // 상태값 초기화
		this.imageUrl = imageUrl;
		this.kakaoLink = kakaoLink;
		this.questionLink = questionLink;
		this.method = method;
		this.frequency = frequency;
		this.recruitmentStartAt = recruitmentStartAt;
		this.recruitmentEndAt = recruitmentEndAt;
		this.member = member;
	}

	public static Study createStudy(
		String title,
		String content,
		Integer recruitmentCount,
		StudyType type,
		String kakaoLink,
		String questionLink,
		StudyMethod method,
		Frequency frequency,
		LocalDateTime recruitmentStartAt,
		LocalDateTime recruitmentEndAt,
		Member member) {
		return Study.builder()
			.title(title)
			.content(content)
			.recruitmentCount(recruitmentCount)
			.type(type)
			.kakaoLink(kakaoLink)
			.questionLink(questionLink)
			.method(method)
			.frequency(frequency)
			.recruitmentStartAt(recruitmentStartAt)
			.recruitmentEndAt(recruitmentEndAt)
			.member(member)
			.build();
	}
	public static Study create(Member member, StudyVo vo) {
		return Study.builder()
			.title(vo.title())
			.content(vo.content())
			.recruitmentCount(vo.recruitmentCount())
			.type(vo.type())
			.recruitmentStartAt(vo.recruitmentStartAt())
			.recruitmentEndAt(vo.recruitmentEndAt())
			.kakaoLink(vo.kakaoLink())
			.questionLink(vo.questionLink())
			.method(vo.method())
			.frequency(vo.frequency())
			.member(member)
			.build();
	}

	public void updateStudy(
		String title,
		String content,
		Integer recruitmentCount,
		StudyMethod method,
		Frequency frequency,
		String kakoLink,
		String questionLink,
		LocalDateTime recruitmentStartAt,
		LocalDateTime recruitmentEndAt) {
		this.title = title;
		this.content = content;
		this.recruitmentCount = recruitmentCount;
		this.method = method;
		this.frequency = frequency;
		this.kakaoLink = kakoLink;
		this.questionLink = questionLink;
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

	public void addStudyTagMap(StudyTagMap tagMap) {
		this.studyTagMaps.add(tagMap);
		tagMap.setStudy(this);
	}

	public void addParticipantsCount() {
		this.participantsCount++;
	}

	public void changeStudyRecruitmentStatus(RecruitmentStatus status) {
		this.recruitmentStatus = status;
	}

}
