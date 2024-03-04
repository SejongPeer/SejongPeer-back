package com.sejong.sejongpeer.domain.auth.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.dto.request.TokenReissueRequest;
import com.sejong.sejongpeer.domain.auth.dto.response.SignInResponse;
import com.sejong.sejongpeer.domain.auth.dto.response.TokenReissueResponse;
import com.sejong.sejongpeer.domain.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. [인증]", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
	private final AuthService authService;

	@Operation(summary = "로그인", description = "토큰 발급을 위해 로그인을 진행합니다.")
	@PostMapping("/sign-in")
	public SignInResponse signIn(@Valid @RequestBody SignInRequest request) {
		return authService.signIn(request);
	}

	@Operation(summary = "Access Token 재발급", description = "Refresh Token을 이용하여 Access Token을 재발급합니다.")
	@PostMapping("/reissue")
	public TokenReissueResponse reissue(@Valid @RequestBody TokenReissueRequest request) {
		return authService.reissue(request);
	}
}
