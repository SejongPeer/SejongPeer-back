package com.sejong.sejongpeer.domain.buddy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BuddyMatched {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buddy_id_1")
	private Buddy buddyId1;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "buddy_id_2")
	private Buddy buddyId2;


}
