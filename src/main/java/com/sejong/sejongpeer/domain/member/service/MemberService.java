package com.sejong.sejongpeer.domain.member.service;

import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import com.sejong.sejongpeer.domain.auth.repository.RefreshTokenRepository;
import com.sejong.sejongpeer.domain.member.dto.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) { // TODO: 이메일, 학번 중복 여부 체크
        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = Member.create(request, encodedPassword);

        RefreshToken refreshToken = RefreshToken.builder().member(member).token("").build();
        refreshTokenRepository.save(refreshToken);

        memberRepository.save(member);
    }
}
