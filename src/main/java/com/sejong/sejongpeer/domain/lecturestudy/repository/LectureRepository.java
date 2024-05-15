package com.sejong.sejongpeer.domain.lecturestudy.repository;

import com.sejong.sejongpeer.domain.lecturestudy.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

	List<Lecture> findAllByCollege(String college);
}
