package com.sejong.sejongpeer.global.util;

import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public HttpHeaders generateTokenHeader(Map<String, String> tokens) {
        HttpHeaders headers = new HttpHeaders();

        for (Map.Entry<String, String> entry : tokens.entrySet()) {
            ResponseCookie cookie =
                    ResponseCookie.from(entry.getKey(), entry.getValue())
                            .path("/")
                            .maxAge(60 * 60 * 24 * 30) // TODO: 조정 필요
                            .secure(true)
                            .sameSite("None") // TODO: 추후 실행환경에 따라 정책 변경
                            .httpOnly(false)
                            .build();

            headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        return headers;
    }
}
