package com.sejong.sejongpeer.global.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public HttpHeaders generateTokenHeader(String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();

        ResponseCookie accessTokenCookie = generateResponseCookie("accessToken", accessToken);
        ResponseCookie refreshTokenCookie = generateResponseCookie("refreshToken", refreshToken);

        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        return headers;
    }

    private ResponseCookie generateResponseCookie(String headerName, String value) {
        return ResponseCookie.from(headerName, value)
                .path("/")
                .maxAge(60 * 60 * 24 * 30) // TODO: 조정 필요
                .secure(true)
                .sameSite("None") // TODO: 추후 실행환경에 따라 정책 변경
                .httpOnly(false)
                .build();
    }
}
