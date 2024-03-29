package com.sejong.sejongpeer.domain.buddy.entity.buddymatched;

import org.hibernate.annotations.Comment;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.type.BuddyMatchedStatus;
import com.sejong.sejongpeer.domain.common.BaseAuditEntity;

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
public class BuddyMatched extends BaseAuditEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "owner")
	private Buddy owner;

	@OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "partner")
	private Buddy partner;

	@Comment("매칭 상태")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BuddyMatchedStatus status;

	@Builder(access = AccessLevel.PRIVATE)
	private BuddyMatched(Buddy ownerBuddy, Buddy partnerBuddy, BuddyMatchedStatus status) {
		this.owner = ownerBuddy;
		this.partner = partnerBuddy;
		this.status = status;
	}

	public static BuddyMatched registerMatchingPair(Buddy ownerBuddy, Buddy partnerBuddy) {
		return BuddyMatched.builder()
			.ownerBuddy(ownerBuddy)
			.partnerBuddy(partnerBuddy)
			.status(BuddyMatchedStatus.IN_PROGRESS)
			.build();
	}

	public void changeStatus(BuddyMatchedStatus status) {
		this.status = status;
	}

}
