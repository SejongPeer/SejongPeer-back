package com.sejong.sejongpeer.domain.auth.api;

import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.dto.response.SignInResponse;
import com.sejong.sejongpeer.domain.auth.service.AuthService;
import com.sejong.sejongpeer.global.util.CookieUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        SignInResponse response = authService.signIn(request);

        HttpHeaders headers =
                cookieUtil.generateTokenHeader(response.accessToken(), response.refreshToken());

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/test") // TODO: 테스트용이므로 추후 삭제 필요
    public void test() {
        System.out.println("test");
    }
}
