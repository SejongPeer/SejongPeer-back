package com.sejong.sejongpeer.domain.college.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;

public interface CollegeMajorRepository extends JpaRepository<CollegeMajor, Long> {

	@Query("SELECT DISTINCT cm.college FROM CollegeMajor cm")
	List<String> findAllColleges();

	List<CollegeMajor> findAllByCollege(String college);
}
