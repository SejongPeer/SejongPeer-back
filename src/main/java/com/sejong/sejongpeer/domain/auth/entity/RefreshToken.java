package com.sejong.sejongpeer.domain.auth.entity;

import com.sejong.sejongpeer.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Column(columnDefinition = "char(36)")
    private String memberId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String token;

    @Builder
    private RefreshToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public void renewToken(String token) {
        this.token = token;
    }
}
