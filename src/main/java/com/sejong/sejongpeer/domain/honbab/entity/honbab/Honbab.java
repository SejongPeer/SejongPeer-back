package com.sejong.sejongpeer.domain.honbab.entity.honbab;

import com.sejong.sejongpeer.domain.common.BaseAuditEntity;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.GenderOption;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.MenuCategoryOption;
import com.sejong.sejongpeer.domain.member.entity.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Honbab extends BaseAuditEntity {
	@Id
	private Long id;

	@Enumerated(EnumType.STRING)
	private HonbabStatus status;

	@Enumerated(EnumType.STRING)
	private GenderOption genderOption;

	@Enumerated(EnumType.STRING)
	private MenuCategoryOption menuCategoryOption;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	public void changeStatus(HonbabStatus status) {
		this.status = status;
	}
}
