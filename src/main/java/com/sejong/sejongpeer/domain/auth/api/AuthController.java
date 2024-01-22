package com.sejong.sejongpeer.domain.auth.api;

import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.service.AuthService;
import com.sejong.sejongpeer.global.util.CookieUtil;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final CookieUtil cookieUtil;

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@Valid @RequestBody SignInRequest request) {
        Map<String, String> tokens = authService.signIn(request);
        HttpHeaders tokenHeaders = cookieUtil.generateTokenHeader(tokens);

        return ResponseEntity.ok().headers(tokenHeaders).build();
    }
}
