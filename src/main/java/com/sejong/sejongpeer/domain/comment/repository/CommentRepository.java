package com.sejong.sejongpeer.domain.comment.repository;

import com.sejong.sejongpeer.domain.comment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
	List<Comment> findAllByStudyId(Long study_id);
}
