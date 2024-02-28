package com.sejong.sejongpeer.domain.honbab.service;

import com.sejong.sejongpeer.domain.honbab.dto.response.MatchingPartnerInfoResponse;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.HonbabMatched;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabMatchedRepository;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HonbabService {

	private final HonbabRepository honbabRepository;
	private final HonbabMatchedRepository honbabMatchedRepository;

	public MatchingPartnerInfoResponse getPartnerInfo(String memberId) {

		Honbab lastestHonbab = getLastestHonbabByMemberId(memberId).orElseThrow(() -> new CustomException(ErrorCode.HONBAB_NOT_FOUND));
		HonbabMatched selectedHonbabMatched = getLastestHonbabMatchedByHonbab(lastestHonbab).orElseThrow(() -> new CustomException(ErrorCode.TARGET_HONBAB_NOT_FOUND));
		Honbab targetHonbab = getHonbabFriend(selectedHonbabMatched, lastestHonbab);
		Member targetHonbabMember = targetHonbab.getMember();

		return MatchingPartnerInfoResponse.of(targetHonbabMember, targetHonbab);

	}

	private Optional<Honbab> getLastestHonbabByMemberId(String memberId) {
		return honbabRepository.findLastHonbabByMemberId(memberId);
	}

	private Optional<HonbabMatched> getLastestHonbabMatchedByHonbab(Honbab applicant) {
		// 만들어진 HonbabMatched의 status는 항상 MATCHING_COMPLETED
		return honbabMatchedRepository.findLatestByOwnerOrPartner(applicant);
	}

	private Honbab getHonbabFriend(HonbabMatched selectedMatched, Honbab applicant) {
		if (selectedMatched.getOwner() == applicant) {
			return selectedMatched.getPartner();
		}
		return selectedMatched.getOwner();
	}
}
