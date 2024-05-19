package com.sejong.sejongpeer.domain.externalactivity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.externalactivity.entity.ExternalActivity;

public interface ExternalActivityRepository extends JpaRepository<ExternalActivity, Long> {
}
