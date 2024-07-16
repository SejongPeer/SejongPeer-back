package com.sejong.sejongpeer.domain.scrap.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.scrap.entity.Scrap;
import com.sejong.sejongpeer.domain.scrap.entity.ScrapType;
import com.sejong.sejongpeer.domain.study.entity.Study;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
	List<Scrap> findByTypeAndStudy(ScrapType type, Study study);

	Long countByStudy(Study study);
}
