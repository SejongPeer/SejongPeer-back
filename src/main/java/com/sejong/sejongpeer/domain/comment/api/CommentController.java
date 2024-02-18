package com.sejong.sejongpeer.domain.comment.api;

import com.sejong.sejongpeer.domain.comment.dto.response.CommentFindResponse;
import com.sejong.sejongpeer.domain.comment.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {
	private final CommentService commentService;

	@GetMapping("/study/{studyId}/comments")
	public List<CommentFindResponse> getComments(@PathVariable(name = "studyId") Long studyId) {
		return commentService.getComments(studyId);
	}

}
