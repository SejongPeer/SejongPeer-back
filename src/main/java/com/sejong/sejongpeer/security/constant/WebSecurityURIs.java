package com.sejong.sejongpeer.security.constant;

import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class WebSecurityURIs {
    public static final List<String> PUBLIC_URIS =
            List.of(
                    "/api/v1/auth/sign-in",
                    "/api/v1/member/**",
                    "/api/v1/member/help/**",
                    "/api/v1/study/**",
                    "/swagger-ui/**");
    public static final List<String> CORS_ALLOW_URIS =
            List.of("http://localhost:3000", "https://sejongpeer.com");
}
