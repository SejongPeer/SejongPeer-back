package com.sejong.sejongpeer.security.filter;

import com.sejong.sejongpeer.security.constant.HeaderConstant;
import com.sejong.sejongpeer.security.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        if (accessToken == null || refreshToken == null) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED, "AccessToken 또는 RefreshToken이 없습니다.");
            return;
        }

        boolean isAccessTokenValid = jwtProvider.isTokenValid(accessToken, true);
        boolean isRefreshTokenValid = jwtProvider.isTokenValid(refreshToken, false);

        // AccessToken가 검증된 경우 인증 OK
        if (accessToken != null && isAccessTokenValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // AccessToken이 만료되었고 RefreshToken도 만료된 경우, 인증 실패
        if (!isAccessTokenValid && !isRefreshTokenValid) {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "AccessToken이 만료되었고 RefreshToken도 만료되었습니다.");
            return;
        }

        // AccessToken이 만료되었으나 RefreshToken이 유효한 경우, AccessToken 재발급
        if (!isAccessTokenValid && isRefreshTokenValid) {
            accessToken = jwtProvider.reissueAccessToken(refreshToken);
            response.setHeader("accessToken", accessToken);
        }

        SecurityContextHolder.getContext()
                .setAuthentication(jwtProvider.getAuthentication(accessToken));
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return path.equals("/api/v1/auth/sign-in") || path.equals("/api/v1/member/sign-up");
    }
}
