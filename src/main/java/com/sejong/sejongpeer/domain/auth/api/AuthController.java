package com.sejong.sejongpeer.domain.auth.api;

import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.service.AuthService;
import com.sejong.sejongpeer.global.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1. [인증]", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "로그인", description = "토큰 발급을 위해 로그인을 진행합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@Valid @RequestBody SignInRequest request) {
        Map<String, String> tokens = authService.signIn(request);
        HttpHeaders tokenHeaders = cookieUtil.generateTokenHeader(tokens);

        return ResponseEntity.ok().headers(tokenHeaders).build();
    }
}
