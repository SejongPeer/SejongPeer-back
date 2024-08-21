package com.sejong.sejongpeer.domain.auth.service;

import com.sejong.sejongpeer.domain.auth.repository.RefreshTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.sejong.sejongpeer.domain.auth.dto.request.SejongAuthRequest;
import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.dto.response.SejongAuthClientResponse;
import com.sejong.sejongpeer.domain.auth.dto.response.SejongAuthResponse;
import com.sejong.sejongpeer.domain.auth.dto.response.SignInResponse;
import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.security.util.JwtProvider;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private static final String SEJONG_AUTH_BASE_URL = "https://auth.imsejong.com/auth?method=ClassicSession";
	private final WebClient webClient;

	public SignInResponse signIn(SignInRequest request) {
		Member member =
			memberRepository
				.findByAccount(request.account())
				.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}

		String accessToken = jwtProvider.generateAccessToken(member.getId());
		String refreshToken = jwtProvider.generateRefreshToken(member.getId());

		renewRefreshToken(member, refreshToken);

		return SignInResponse.of(accessToken, refreshToken, member);
	}

	private void renewRefreshToken(Member member, String token) {
		RefreshToken refreshToken = refreshTokenRepository.findById(member.getId()).orElse(null);

		if (refreshToken == null) { // 최초가입 후 로그인일 경우 Refresh Token 존재하지 않음
			initRefreshToken(member, token);
		} else {
			refreshToken.renewToken(token);
		}
	}

	private void initRefreshToken(Member member, String token) {
		RefreshToken refreshToken = RefreshToken.builder().member(member).token(token).build();

		refreshTokenRepository.save(refreshToken);
	}

	public SejongAuthClientResponse sejongAuthLogin(SejongAuthRequest request) {

		SejongAuthResponse authResponse = webClient.post()
			.uri(SEJONG_AUTH_BASE_URL)
			.bodyValue(request)
			.retrieve()
			.bodyToMono(SejongAuthResponse.class)
			.block();
		return SejongAuthClientResponse.of(
			authResponse.msg(),
			authResponse.result().body().grade(),
			authResponse.result().body().major(),
			authResponse.result().body().name(),
			authResponse.result().body().status(),
			authResponse.result().is_auth(),
			authResponse.result().status_code(),
			authResponse.result().success()
		);
	}
}
