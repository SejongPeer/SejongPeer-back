package com.sejong.sejongpeer.domain.honbab.entity.honbabmatched;

import org.hibernate.annotations.Comment;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.type.HonbabMatchedStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class HonbabMatched {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner")
	private Honbab owner;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "partner")
	private Honbab partner;

	@Comment("매칭 상태")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private HonbabMatchedStatus status;

	@Builder(access = AccessLevel.PRIVATE)
	private HonbabMatched(Honbab owner, Honbab partner, HonbabMatchedStatus status) {
		this.owner = owner;
		this.partner = partner;
		this.status = status;
	}

	public static HonbabMatched registerMatchingPair(Honbab me, Honbab partner) {
		return HonbabMatched.builder()
			.owner(me)
			.partner(partner)
			.status(HonbabMatchedStatus.MATCHING_COMPLETED)
			.build();
	}
}
