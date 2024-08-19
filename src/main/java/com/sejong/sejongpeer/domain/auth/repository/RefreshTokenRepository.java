package com.sejong.sejongpeer.domain.auth.repository;

import java.util.Optional;

import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
	Optional<RefreshToken> findByMemberId(String memberId);
}
