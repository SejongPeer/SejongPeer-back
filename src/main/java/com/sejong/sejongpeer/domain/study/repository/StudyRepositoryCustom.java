package com.sejong.sejongpeer.domain.study.repository;

import com.sejong.sejongpeer.domain.study.entity.Study;

import org.springframework.data.domain.Slice;

public interface StudyRepositoryCustom {
	Slice<Study> findStudySlice(int size, Long lastId);
}
