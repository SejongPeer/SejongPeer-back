package com.sejong.sejongpeer.security.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.security.constant.HeaderConstant;
import com.sejong.sejongpeer.security.constant.WebSecurityURIs;
import com.sejong.sejongpeer.security.util.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(
		@NotNull HttpServletRequest request,
		@NotNull HttpServletResponse response,
		@NotNull FilterChain filterChain)
		throws ServletException, IOException {
		String accessToken = jwtProvider.resolveToken(request, HeaderConstant.ACCESS_TOKEN_HEADER);
		String refreshToken =
			jwtProvider.resolveToken(request, HeaderConstant.REFRESH_TOKEN_HEADER);

		if (accessToken == null) {
			return;
		}

		boolean isAccessTokenValid = jwtProvider.isTokenValid(accessToken, true);
		boolean isRefreshTokenValid = jwtProvider.isTokenValid(refreshToken, false);

		// AccessToken이 만료되었고 RefreshToken도 만료된 경우, 인증 실패
		if (!isAccessTokenValid && !isRefreshTokenValid) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		}

		// AccessToken이 만료되었으나 RefreshToken이 유효한 경우, AccessToken 재발급
		if (!isAccessTokenValid && isRefreshTokenValid) {
			accessToken = jwtProvider.reissueAccessToken(refreshToken);
			response.setHeader(
				"accessToken",
				accessToken); // FIXME: accessToken 만료 시 재발급은 따로 처리해야 함. 매 요청마다 프론트에서는 이전 accessToken을 갖고 있을 것임
		}

		String memberId = jwtProvider.extractMemberId(accessToken, true);
		SecurityContextHolder.getContext()
			.setAuthentication(jwtProvider.getAuthentication(memberId));
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return WebSecurityURIs.PUBLIC_URIS.stream()
			.anyMatch(uri -> new AntPathRequestMatcher(uri).matches(request));
	}
}
