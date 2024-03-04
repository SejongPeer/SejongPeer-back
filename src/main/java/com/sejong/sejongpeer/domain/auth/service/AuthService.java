package com.sejong.sejongpeer.domain.auth.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.dto.request.TokenReissueRequest;
import com.sejong.sejongpeer.domain.auth.dto.response.SignInResponse;
import com.sejong.sejongpeer.domain.auth.dto.response.TokenReissueResponse;
import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import com.sejong.sejongpeer.domain.auth.repository.RefreshTokenRepository;
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

	public SignInResponse signIn(SignInRequest request) {
		Member member =
			memberRepository
				.findByAccount(request.account())
				.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
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

	@Transactional(readOnly = true)
	public TokenReissueResponse reissue(TokenReissueRequest request) {
		if (!jwtProvider.isTokenValid(request.refreshToken(), false)) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		}

		String memberId = jwtProvider.extractMemberId(request.refreshToken(), false);

		RefreshToken refreshToken =
			refreshTokenRepository
				.findById(memberId)
				.orElseThrow(
					() -> new CustomException(ErrorCode.UNAUTHORIZED));    // TODO: redis 도입 이후, redis 에서 조회하도록 변경

		if (!refreshToken.getToken()
			.equals(request.refreshToken())) {    // 요청받은 refresh token 과 불러온 refresh token 같은지 검증
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}

		String accessToken = jwtProvider.generateAccessToken(memberId);
		return new TokenReissueResponse(accessToken);
	}
}
