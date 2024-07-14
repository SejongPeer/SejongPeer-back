package com.sejong.sejongpeer.domain.studyrelation.dto.request;

import jakarta.validation.constraints.NotNull;

public record StudyMatchingRequest(
	@NotNull(message = "스터디 게시글 id를 입력해주세요.") Long studyId,
	@NotNull(message = "수락/거절 하려는 지원자의 닉네임을 입력해주세요.") String applicantNickname,
	@NotNull(message = "스터디 지원자를 수락하려면 true, 거절하려면 false로 요청해주세요.") boolean isAccept
) {
}
