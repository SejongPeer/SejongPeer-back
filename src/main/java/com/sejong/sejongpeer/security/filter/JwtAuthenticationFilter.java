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
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}

		boolean isAccessTokenValid = jwtProvider.isTokenValid(accessToken, true);

		// AccessToken이 만료된 경우
		if (!isAccessTokenValid) {
			throw new CustomException(ErrorCode.TOKEN_EXPIRED);
		}

		String memberId = jwtProvider.extractMemberId(accessToken, true);
		SecurityContextHolder.getContext()
			.setAuthentication(jwtProvider.getAuthentication(memberId));
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return WebSecurityURIs.PUBLIC_URIS.stream()
			.anyMatch(uri -> new AntPathRequestMatcher(uri).matches(request))
			|| WebSecurityURIs.SWAGGER_URIS.stream()
			.anyMatch(uri -> new AntPathRequestMatcher(uri).matches(request));
	}
}
