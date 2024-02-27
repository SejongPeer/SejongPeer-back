package com.sejong.sejongpeer.domain.buddy.dto.response;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;

public record PartnerInfoResponse(
	CollegeMajor collegeMajor,
	Integer grade
) {
}
