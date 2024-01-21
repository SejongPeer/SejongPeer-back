package com.sejong.sejongpeer.domain.auth.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.security.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {
	private final JwtProvider jwtProvider;

	public Map<String, String> generateTokens(String memberId) {
		String accessToken = jwtProvider.generateAccessToken(memberId);
		String refreshToken = jwtProvider.generateRefreshToken(memberId);

		return Map.of(
			"accessToken", accessToken,
			"refreshToken", refreshToken
		);
	}
}
