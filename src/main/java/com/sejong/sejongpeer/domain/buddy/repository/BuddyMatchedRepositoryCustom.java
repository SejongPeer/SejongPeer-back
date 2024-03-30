package com.sejong.sejongpeer.domain.buddy.repository;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;

import java.util.Optional;

public interface BuddyMatchedRepositoryCustom {
	Optional<BuddyMatched> findByOwnerOrPartner(Buddy owner);
}
