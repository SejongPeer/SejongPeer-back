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
    @JoinColumn(name = "buddy_id_1")
    private Buddy buddyId1;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buddy_id_2")
    private Buddy buddyId2;

    @Comment("매칭 상태")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BuddyMatchedStatus buddyMatchedStatus;
}
