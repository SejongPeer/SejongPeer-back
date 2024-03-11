package com.sejong.sejongpeer.domain.auth.api;

import com.sejong.sejongpeer.domain.auth.dto.request.SejongAuthRequest;
import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.dto.response.SejongAuthClientResponse;
import com.sejong.sejongpeer.domain.auth.dto.response.SejongAuthResponse;
import com.sejong.sejongpeer.domain.auth.dto.response.SignInResponse;
import com.sejong.sejongpeer.domain.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.apache.coyote.Response;
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

	@Operation(summary = "로그인", description = "토큰 발급을 위해 로그인을 진행합니다.")
	@PostMapping("/sign-in")
	public SignInResponse signIn(@Valid @RequestBody SignInRequest request) {
		return authService.signIn(request);
	}

	@Operation(summary = "세종대학교 학생 인증", description = "세종대학교 학생 인증을 시도합니다.")
	@PostMapping("/sejong-auth")
	public SejongAuthClientResponse sejongAuthValidate(@Valid @RequestBody SejongAuthRequest request) {
		return authService.sejongAuthLogin(request);
	}
}
