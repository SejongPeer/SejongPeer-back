package com.sejong.sejongpeer.domain.honbab.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;
import com.sejong.sejongpeer.domain.honbab.entity.honbabmatched.HonbabMatched;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabMatchedRepository;
import com.sejong.sejongpeer.domain.honbab.repository.HonbabRepository;
import com.sejong.sejongpeer.domain.honbab.util.HonbabFilter;
import com.sejong.sejongpeer.infra.sms.service.SmsService;
import com.sejong.sejongpeer.infra.sms.service.SmsText;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class HonbabMatchingService {
	private final SmsService smsService;

	private final HonbabRepository honbabRepository;
	private final HonbabMatchedRepository honbabMatchedRepository;

	public void executeMatching() {
		List<Honbab> candidates = honbabRepository.findAllByStatus(HonbabStatus.IN_PROGRESS);
		List<HonbabMatched> honbabMatcheds = new ArrayList<>();

		for (Honbab me : candidates) {
			if (isWaitTimeExceeded(me)) {
				handleTimeOut(me);
			} else {
				HonbabMatched honbabMatched = matchHonbab(candidates, me);
				if (honbabMatched != null) {
					honbabMatcheds.add(honbabMatched);
				}
			}
		}

		honbabRepository.saveAll(candidates);    // matchHonbab에서 변경된 상태 벌크로 업데이트
		honbabMatchedRepository.saveAll(honbabMatcheds);
	}

	public HonbabMatched matchHonbabWhenRegister(Honbab me) {
		List<Honbab> candidates = honbabRepository.findAllByStatus(HonbabStatus.IN_PROGRESS);
		Honbab partner = findSuitableHonbab(candidates, me);

		if (partner == null) {
			return null;
		}

		HonbabMatched honbabMatched = HonbabMatched.registerMatchingPair(me, partner);

		partner.changeStatus(HonbabStatus.MATCHING_COMPLETED);
		me.changeStatus(HonbabStatus.MATCHING_COMPLETED);

		sendMatchingMessage(me);
		sendMatchingMessage(partner);

		return honbabMatchedRepository.save(honbabMatched);
	}

	public HonbabMatched matchHonbab(List<Honbab> candidates, Honbab me) {
		Honbab partner = findSuitableHonbab(candidates, me);

		if (partner == null) {
			return null;
		}

		HonbabMatched honbabMatched = HonbabMatched.registerMatchingPair(me, partner);

		partner.changeStatus(HonbabStatus.MATCHING_COMPLETED);
		me.changeStatus(HonbabStatus.MATCHING_COMPLETED);

		sendMatchingMessage(me);
		sendMatchingMessage(partner);

		return honbabMatched;
	}

	private Honbab findSuitableHonbab(List<Honbab> candidates, Honbab me) {
		return candidates.stream()
			.filter(candidate -> candidate.getId() != me.getId())
			.filter(candidate -> candidate.getStatus() == HonbabStatus.IN_PROGRESS)
			.filter(candidate -> HonbabFilter.filterSutiableGender(candidate, me))
			.filter(candidate -> HonbabFilter.filterSuitableMenuCategory(candidate, me))
			.findFirst()
			.orElse(null);
	}

	private void sendMatchingMessage(Honbab me) {
		String phoneNumber = me.getMember().getPhoneNumber();

		smsService.sendSms(phoneNumber, SmsText.MATCHING_FOUND_HONBAB);
	}

	private boolean isWaitTimeExceeded(Honbab honbab) {
		return Duration.between(honbab.getCreatedAt(), LocalDateTime.now()).getSeconds() > honbab.getWaitTime().getSeconds();
	}

	private void handleTimeOut(Honbab honbab) {
		honbab.changeStatus(HonbabStatus.TIME_OUT);
	}
}
