package com.sejong.sejongpeer.domain.comment.dto.response;

import com.sejong.sejongpeer.domain.comment.entity.Comment;

public record CommentCreateResponse(Long commentId) {
	public static CommentCreateResponse from(Comment comment) {
		return new CommentCreateResponse(comment.getId());
	}
}
