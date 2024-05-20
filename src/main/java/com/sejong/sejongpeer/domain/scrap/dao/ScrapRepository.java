package com.sejong.sejongpeer.domain.scrap.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.scrap.entity.Scrap;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
}
