package com.sejong.sejongpeer.domain.study.repository;

import org.springframework.data.domain.Slice;

import com.sejong.sejongpeer.domain.study.entity.Study;

public interface StudyRepositoryCustom {
	Slice<Study> findStudySlice(int size, Long lastId);
}
