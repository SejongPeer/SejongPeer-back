package com.sejong.sejongpeer.security.util;

import com.sejong.sejongpeer.security.constant.HeaderConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtProvider {
    @Value("${jwt.access-token-expiration}")
    public long ACCESS_TOKEN_VALID_MILL_TIME;

    @Value("${jwt.refresh-token-expiration}")
    private long REFRESH_TOKEN_VALID_MILL_TIME;

    @Value("${jwt.access-token-key}")
    private String ACCESS_SECRET_KEY;

    @Value("${jwt.refresh-token-key}")
    private String REFRESH_SECRET_KEY;

    public String resolveToken(@Nullable HttpServletRequest request, String header) {
        String authHeader = request.getHeader(header);
        if (authHeader == null) {
            return null;
        }

        String token = authHeader.replace(HeaderConstant.TOKEN_PREFIX, "");

        return token;
    }

    public String generateAccessToken(@Nullable String memberId) {
        final Date now = new Date();

        // access token 생성
        String accessToken =
                Jwts.builder()
                        .setSubject(memberId)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_MILL_TIME))
                        .signWith(getAccessTokenKey())
                        .compact();

        return accessToken;
    }

    public String generateRefreshToken(@Nullable String memberId) {
        final Date now = new Date();

        // 토큰 생성
        String refreshToken =
                Jwts.builder()
                        .setSubject(memberId)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_MILL_TIME))
                        .signWith(getRefreshTokenKey())
                        .compact();

        return refreshToken;
    }

    public <T> T extractClaim(
            String token, boolean isAccessToken, Function<Claims, T> claimResolver) {
        Claims claims = extractAllAccessTokenClaims(token, isAccessToken);

        return claimResolver.apply(claims);
    }

    private Claims extractAllAccessTokenClaims(String token, boolean isAccessToken) {
        Key signingKey = isAccessToken ? getAccessTokenKey() : getRefreshTokenKey();

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String extractMemberId(String token, boolean isAccessToken) {
        return extractClaim(token, isAccessToken, (claims) -> claims.get("sub", String.class));
    }

    public Date extractExpiration(String token, boolean isAccessToken) {
        return extractClaim(token, isAccessToken, Claims::getExpiration);
    }

    private Key getAccessTokenKey() {
        byte[] keyBytes = ACCESS_SECRET_KEY.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getRefreshTokenKey() {
        byte[] keyBytes = REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, boolean isAccessToken) {
        try {
            return (!isTokenExpired(token, isAccessToken));
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
        } catch (JwtException e) {
            log.error("JWT Token is not valid", e);
        }

        return false;
    }

    private boolean isTokenExpired(String token, boolean isAccessToken) {
        return extractExpiration(token, isAccessToken).before(new Date());
    }

    public String reissueAccessToken(String refreshToken) {
        if (!isTokenValid(refreshToken, false)) {
            throw new JwtException("RefreshToken is not valid");
        }

        String memberId = extractMemberId(refreshToken, false);

        return generateAccessToken(memberId);
    }

    public Authentication getAuthentication(String memberId) {
        return new UsernamePasswordAuthenticationToken(memberId, null, null);
    }
}
