package com.sejong.sejongpeer.domain.auth.service;

import com.sejong.sejongpeer.domain.auth.dto.request.SignInRequest;
import com.sejong.sejongpeer.domain.auth.dto.response.SignInResponse;
import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import com.sejong.sejongpeer.domain.auth.repository.RefreshTokenRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.security.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public SignInResponse signIn(SignInRequest request) {
        Member member =
                memberRepository
                        .findByAccount(request.account())
                        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtProvider.generateAccessToken(member.getId());
        String refreshToken = jwtProvider.generateRefreshToken(member.getId());

        renewRefreshToken(member, refreshToken);

        return new SignInResponse(accessToken, refreshToken);
    }

    private void renewRefreshToken(Member member, String token) {
        RefreshToken refreshToken =
                refreshTokenRepository
                        .findById(member.getId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        refreshToken.renewToken(token);
    }
}
