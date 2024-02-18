package com.sejong.sejongpeer.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentPostRequest(
	@NotBlank String content,
	Long parentId) {
}
