package com.sejong.sejongpeer.domain.honbab.dto.response;

import com.sejong.sejongpeer.domain.honbab.entity.honbab.Honbab;
import com.sejong.sejongpeer.domain.honbab.entity.honbab.type.HonbabStatus;

public record HonbabMatchingStatusResponse(HonbabStatus status) {
	public static HonbabMatchingStatusResponse from(Honbab honbab) {
		return new HonbabMatchingStatusResponse(honbab.getStatus());
	}
}
