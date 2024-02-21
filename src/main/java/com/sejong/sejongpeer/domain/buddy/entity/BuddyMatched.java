package com.sejong.sejongpeer.domain.buddy.entity;

import com.sejong.sejongpeer.domain.buddy.entity.type.BuddyMatchedStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BuddyMatched {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private Buddy ownerBuddy;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner")
    private Buddy partnerBuddy;

    @Comment("매칭 상태")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuddyMatchedStatus buddyMatchedStatus;
}
