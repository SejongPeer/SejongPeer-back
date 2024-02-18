package com.sejong.sejongpeer.domain.comment.api;

import com.sejong.sejongpeer.domain.comment.dto.request.CommentCreateRequest;
import com.sejong.sejongpeer.domain.comment.dto.response.CommentCreateResponse;
import com.sejong.sejongpeer.domain.comment.dto.response.CommentFindResponse;
import com.sejong.sejongpeer.domain.comment.service.CommentService;
import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.request.StudyUpdateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {
	private final CommentService commentService;

	@PostMapping("/study/{studyId}/comments")
	public ResponseEntity<CommentCreateResponse> studyUpdate(
		@Valid @RequestBody CommentCreateRequest commentCreateRequest, @PathVariable Long studyId) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(commentService.createComment(commentCreateRequest, studyId));
	}

	@GetMapping("/study/{studyId}/comments")
	public List<CommentFindResponse> getComments(@PathVariable(name = "studyId") Long studyId) {
		return commentService.getComments(studyId);
	}

}
