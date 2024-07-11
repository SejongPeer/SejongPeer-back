package com.sejong.sejongpeer.domain.study.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejong.sejongpeer.domain.study.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	Optional<Tag> findByName(String name);
	Optional<Tag> findByHashedName(String hashedName);  // 해시값으로 태그를 찾는 메소드
}
