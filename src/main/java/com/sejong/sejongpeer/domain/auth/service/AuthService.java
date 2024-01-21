package com.sejong.sejongpeer.domain.auth.service;

import java.util.Map;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import com.sejong.sejongpeer.domain.auth.repository.RefreshTokenRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	public Map<String, String> signIn(SignInRequest request) {
		Member member = memberRepository.findByAccount(request.account())
			.orElseThrow(() -> new UsernameNotFoundException("Member not found with account: " + request.account()));

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new UsernameNotFoundException("Member not found with account: " + request.account());
		}

		Map<String, String> tokens = jwtService.generateTokens(member.getId());

		RefreshToken refreshToken = refreshTokenRepository.findById(member.getId())
			.orElseThrow(
				() -> new UsernameNotFoundException("RefreshToken not found with memberId: " + member.getId()));
		refreshToken.renewToken(tokens.get("refreshToken"));

		return tokens;
	}
}
