package com.sejong.sejongpeer.domain.auth.service;

import com.sejong.sejongpeer.security.constant.HeaderConstant;
import com.sejong.sejongpeer.security.util.JwtProvider;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {
    private final JwtProvider jwtProvider;

    public Map<String, String> generateTokens(String memberId) {
        String accessToken = jwtProvider.generateAccessToken(memberId);
        String refreshToken = jwtProvider.generateRefreshToken(memberId);

        return Map.of(
                HeaderConstant.ACCESS_TOKEN_HEADER, accessToken,
                HeaderConstant.REFRESH_TOKEN_HEADER, refreshToken);
    }
}
