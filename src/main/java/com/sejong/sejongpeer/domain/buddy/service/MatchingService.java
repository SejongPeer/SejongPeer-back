package com.sejong.sejongpeer.domain.buddy.service;

import static com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.buddy.entity.buddy.Buddy;
import com.sejong.sejongpeer.domain.buddy.entity.buddy.type.BuddyStatus;
import com.sejong.sejongpeer.domain.buddy.entity.buddymatched.BuddyMatched;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyMatchedRepository;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;
import com.sejong.sejongpeer.domain.buddy.util.BuddyFilter;
import com.sejong.sejongpeer.infra.sms.service.SmsService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MatchingService {
	private final SmsService smsService;

	private final BuddyRepository buddyRepository;
	private final BuddyMatchedRepository buddyMatchedRepository;

	/**
	 * 1. 매칭 대기중인 Buddy를 조회한다.
	 * 2. 각 Buddy와 조건이 일치하는 Buddy를 조회한다. 각각의 조건을 아래와 같다.
	 * - 성별 (동성, 상관없음)
	 * - 단과대 혹은 학과 (동일단과대, 동일학과, 상관없음) - 희망하는 상대 (선배, 동기, 후배, 상관없음), 학번을 통해 검색한다.
	 * - 희망 학년 (1학년, 2학년,3학년, 4학년(혹은 그 이상), 상관없음)
	 * 3-1. 조건에 맞는 Buddy를 찾았을 경우 각각의 Buddy를 상태를 변경한다. (IN_PROGRESS -> FOUND_BUDDY)
	 * 3-2. 각 Buddy를 BuddyMatched에 저장한다.
	 */
	public void executeMatching() {
		List<Buddy> candidates = buddyRepository.findAllByStatus(BuddyStatus.IN_PROGRESS);
		List<BuddyMatched> buddyMatcheds = new ArrayList<>();

		for (Buddy me : candidates) {
			BuddyMatched buddyMatched = matchBuddy(candidates, me);
			if (buddyMatched != null) {
				buddyMatcheds.add(buddyMatched);
			}
		}

		buddyRepository.saveAll(candidates);
		buddyMatchedRepository.saveAll(buddyMatcheds);
	}

	public BuddyMatched matchBuddy(List<Buddy> candidates, Buddy me) {
		Buddy partner = findSuitableBuddy(candidates, me);

		if (partner == null) {
			return null;
		}

		BuddyMatched buddyMatched = BuddyMatched.registerMatchingPair(me, partner);

		partner.changeStatus(FOUND_BUDDY);
		me.changeStatus(FOUND_BUDDY);

		sendMatchingMessage(me);
		sendMatchingMessage(partner);

		return buddyMatched;
	}

	public BuddyMatched matchBuddy(Buddy me) {
		List<Buddy> candidates = buddyRepository.findAllByStatus(BuddyStatus.IN_PROGRESS);

		return matchBuddy(candidates, me);
	}

	private void sendMatchingMessage(Buddy me) {
		String phoneNumber = me.getMember().getPhoneNumber();

		smsService.sendSms(phoneNumber, SmsText.MATCHING_FOUND_BUDDY);
	}

	private Buddy findSuitableBuddy(List<Buddy> candidates, Buddy me) {
		return candidates.stream()
			.filter(candidate -> candidate.getId() != me.getId()) // 본인은 제외 시킴
			.filter(
				candidate ->
					candidate.getStatus()
						== BuddyStatus.IN_PROGRESS) // 이전 과정에서 매칭된 Buddy가 존재할 수 있으므로 한 번 더 필터링
			.filter(candidate -> BuddyFilter.filterSuitableGender(candidate, me))
			.filter(candidate -> BuddyFilter.filterSuitableCollegeMajor(candidate, me))
			.filter(candidate -> BuddyFilter.filterSuitableType(candidate, me))
			.filter(candidate -> BuddyFilter.filterSuitableGrade(candidate, me))
			.findFirst()
			.orElse(null);
	}
}
