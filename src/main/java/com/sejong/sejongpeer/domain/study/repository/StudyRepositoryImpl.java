package com.sejong.sejongpeer.domain.study.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.sejong.sejongpeer.domain.study.entity.Study;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom{
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Slice<Study> findStudySlice(int size, Long lastId) {
		return ;
	}

	private Slice<Study> checkLastPage(
		int size, List<Study> result) {
		boolean hasNext = false;

		// 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
		if (result.size() > size) {
			hasNext = true;
			result.remove(size);
		}
		Pageable pageable = Pageable.unpaged();
		return new SliceImpl<>(result, pageable, hasNext);
	}
}
