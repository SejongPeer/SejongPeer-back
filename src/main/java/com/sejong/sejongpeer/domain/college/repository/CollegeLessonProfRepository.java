package com.sejong.sejongpeer.domain.college.repository;

import com.sejong.sejongpeer.domain.college.entity.CollegeLessonProf;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollegeLessonProfRepository extends JpaRepository<CollegeLessonProf, Long> {

	List<CollegeLessonProf> findAllByCollege(String college);
}
