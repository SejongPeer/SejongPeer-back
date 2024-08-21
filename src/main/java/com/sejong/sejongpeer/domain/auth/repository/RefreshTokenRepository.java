package com.sejong.sejongpeer.domain.auth.repository;

import java.util.Optional;

import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
	Optional<RefreshToken> findByMemberId(String memberId);
}
