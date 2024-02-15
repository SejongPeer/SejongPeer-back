package com.sejong.sejongpeer.domain.college.repository;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CollegeMajorRepository extends JpaRepository<CollegeMajor, Long> {

    @Query("SELECT DISTINCT cm.college FROM CollegeMajor cm")
    List<String> findAllColleges();

    List<CollegeMajor> findAllByCollege(String college);

    Optional<CollegeMajor> findByCollegeAndMajor(String college, String major);
}
