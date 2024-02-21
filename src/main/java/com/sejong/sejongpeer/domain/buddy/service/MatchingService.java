package com.sejong.sejongpeer.domain.buddy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.buddy.entity.Buddy;
import com.sejong.sejongpeer.domain.buddy.repository.BuddyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MatchingService {
	private final BuddyRepository buddyRepository;

	/**
	 * 1. 매칭 대기중인 Buddy를 조회한다.
	 * 2. 각 Buddy와 조건이 일치하는 Buddy를 조회한다. 각각의 조건을 아래와 같다.
	 * - 성별 (동성, 상관없음)
	 * - 단과대 혹은 학과 (동일단과대, 동일학과, 상관없음)
	 * - 희망하는 상대 (선배, 동기, 후배, 상관없음), 학번을 통해 검색한다.
	 * - 희망 학년 (1학년, 2학년, 3학년, 4학년(혹은 그 이상), 상관없음)
	 * 3-1. 조건에 맞는 Buddy를 찾았을 경우 각각의 Buddy를 상태를 변경한다. (IN_PROGRESS -> FOUND_BUDDY)
	 * 3-2. 각 Buddy를 BuddyMatched에 저장한다.
	 */
	public void executeMatching() {
		List<Buddy> candidates = buddyRepository.findByStatus(BuddyStatus.IN_PROGRESS);
		List<BuddyMatched> buddyMatcheds = new ArrayList<>();

		for (Buddy me : candidates) {
			BuddyMatched buddyMatched = matchBuddy(candidates, me);
			if (buddyMatched != null) {
				buddyMatcheds.add(buddyMatched);
			}
		}

		buddyRepository.saveAll(buddies);
		buddyMatchedRepository.saveAll(buddyMatcheds);
	}

	public BuddyMatched matchBuddy(List<Buddy> candidates, Buddy me) {
		Buddy partner = findSuitableBuddy(candidates, me);

		if (partner == null) {
			return null;
		}

		me.changeStatus(BuddyStatus.FOUND_BUDDY);
		partner.changeStatus(BuddyStatus.FOUND_BUDDY);

		return BuddyMatched.builder()    // TODO: BuddyMatched 엔티티 구현 후, 팩토리 메소드로 생성
			.buddy(me)
			.partner(partner)
			.build();
	}

	private Buddy findSuitableBuddy(List<Buddy> candidates, Buddy me) {
		return candidates.stream()
			.filter(candidate -> candidate.getId() != me.getId())    // 본인은 제외 시킴
			.filter(candidate -> candidate.getStatus()
				== BuddyStatus.IN_PROGRESS)    // 이전 과정에서 매칭된 Buddy가 존재할 수 있으므로 한 번 더 필터링
			.filter(candidate -> filterDesiredGender(candidate, me))
			.filter(candidate -> filterDesiredCollegeMajor(candidate, me))
			.filter(candidate -> filterDesiredType(candidate, me))
			.filter(candidate -> filterDesiredGrade(candidate, me))
			.findFirst()
			.orElse(null);
	}

	private static boolean filterDesiredGender(Buddy candidate, Buddy me) {
		// me와 candidate 모두 성별 상관없음일 경우

		// me가 동성을 원할 경우, candidate 성별 선택과 상관없이 동성이기만 하면 됨
		// 선택지가 '상관없음', '동성'만있기에 한 쪽이 동성을 원하는 경우, 다른 한 쪽의 성별 선택은 의미 없음
	}

	private static boolean filterDesiredCollegeMajor(Buddy candidate, Buddy me) {
		// me와 candidate 모두 상관없음일 경우

		// 복전 및 부전공를 선택한 경우에 따라 검색할 전공이 달라짐

		// me가 동일 단과대를 원할 경우, candidate는 상관없음 혹은 동일 단과대

		// me가 동일 학과를 원할 경우, candidate는 상관없음 혹은 동일 학과
	}

	private static boolean filterDesiredType(Buddy candidate, Buddy me) {
		// me와 other 모두 상관없음일 경우

		// me가 원하는 조건이 선배(senior)일 경우 -> candidate는 선배 학번 + 원하는 조건이 상관없음 or 후배(junior)여야함

		// me가 원하는 조건이 후배(junior)일 경우, candidate는 후배 학번 + 원하는 조건이 상관없음 or 선배(senior)여야함

		// me가 원하는 조건이 동기(same)일 경우, candidate는 동일 학번 + 원하는 조건이 상관없음 or 동기(same)여야함

	}

	private static boolean filterDesiredGrade(Buddy candidate, Buddy me) {
		// me와 candidate 모두 상관없음일 경우

		// me가 원하는 학년이 1학년일 경우, candidate 1학년 + 원하는 조건이 상관없음 or 1학년이어야함

		// me가 원하는 학년이 2학년일 경우, candidate 2학년 + 원하는 조건이 상관없음 or 2학년이어야함

		// me가 원하는 학년이 3학년일 경우, candidate 3학년 + 원하는 조건이 상관없음 or 3학년이어야함

		// me가 원하는 학년이 4학년(혹은 그 이상)일 경우, candidate 4학년 + 원하는 조건이 상관없음 or 4학년(혹은 그 이상)이어야함
	}

}
