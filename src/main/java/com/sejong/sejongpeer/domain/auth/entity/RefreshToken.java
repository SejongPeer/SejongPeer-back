package com.sejong.sejongpeer.domain.auth.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@RedisHash(value = "refreshToken", timeToLive = 259200000L / 1000)
public class RefreshToken {
	@Id
	private String memberId;

	@Indexed
	private String token;

	@Builder
	private RefreshToken(String memberId, String token) {
		this.memberId = memberId;
		this.token = token;
	}

	public void renewToken(String token) {
		this.token = token;
	}
}
