package com.sejong.sejongpeer.domain.lecture.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.lecture.entity.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

	List<Lecture> findAllByCollege(String college);
}
