package com.sejong.sejongpeer.domain.comment.service;

import com.sejong.sejongpeer.domain.comment.dto.response.CommentFindResponse;
import com.sejong.sejongpeer.domain.comment.entity.Comment;
import com.sejong.sejongpeer.domain.comment.repository.CommentRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class CommentService {
	private final CommentRepository commentRepository;

	@Transactional(readOnly = true)
	public List<CommentFindResponse> getComments(Long studyId) {
		List<Comment> comments = commentRepository.findAllByStudyId(studyId);

		Map<Long, CommentFindResponse> commentResponseMap = comments.stream()
			.filter(comment -> comment.getParent() == null)
			.collect(Collectors.toMap(
				Comment::getId,
				comment -> new CommentFindResponse(
					comment.getId(),
					comment.getMember().getNickname(),
					comment.getContent(),
					new ArrayList<>()
				)
			));

		comments.stream()
			.filter(comment -> comment.getParent() != null)
			.forEach(comment ->
				commentResponseMap.get(comment.getParent().getId())
					.subComments()
					.add(
						new CommentFindResponse(
							comment.getId(),
							comment.getMember().getNickname(),
							comment.getContent(),
							null)
					)
			);

		return comments.stream()
			.map(comment -> commentResponseMap.get(comment.getId()))
			.collect(Collectors.toList());
	}
}
