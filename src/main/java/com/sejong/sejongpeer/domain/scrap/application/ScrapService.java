package com.sejong.sejongpeer.domain.scrap.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.scrap.dao.ScrapRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ScrapService {

	private final ScrapRepository scrapRepository;
}
