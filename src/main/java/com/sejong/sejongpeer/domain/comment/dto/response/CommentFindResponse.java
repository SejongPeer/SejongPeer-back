package com.sejong.sejongpeer.domain.comment.dto.response;

import java.util.List;

public record CommentFindResponse(
	Long id,
	String nickname,
	String content,
	List<CommentFindResponse> subComments) {
}
