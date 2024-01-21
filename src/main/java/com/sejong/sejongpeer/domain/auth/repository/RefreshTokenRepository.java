package com.sejong.sejongpeer.domain.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}
