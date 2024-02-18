package com.sejong.sejongpeer.domain.comment.service;

import com.sejong.sejongpeer.domain.comment.dto.request.CommentCreateRequest;
import com.sejong.sejongpeer.domain.comment.dto.response.CommentCreateResponse;
import com.sejong.sejongpeer.domain.comment.dto.response.CommentFindResponse;
import com.sejong.sejongpeer.domain.comment.entity.Comment;
import com.sejong.sejongpeer.domain.comment.repository.CommentRepository;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.study.service.StudyService;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
	private final StudyRepository studyRepository;

	public CommentCreateResponse createComment(CommentCreateRequest commentCreateRequest, Long studyId) {
		Study study = studyRepository.findById(studyId)
			.orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));
		Comment parent = commentRepository.findById(commentCreateRequest.parentId())
			.orElseGet(() -> null);

		Comment comment = createCommentEntity(commentCreateRequest, study, parent);
		Comment saveComment = commentRepository.save(comment);
		return CommentCreateResponse.from(saveComment);
	}

	private Comment createCommentEntity(final CommentCreateRequest commentCreateRequest, Study study,
										final Comment parent) {
		return Comment.builder()
			.study(study)
			.parent(parent)
			.content(commentCreateRequest.content())
			.member(Member.builder().build())
			.build();
	}

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
