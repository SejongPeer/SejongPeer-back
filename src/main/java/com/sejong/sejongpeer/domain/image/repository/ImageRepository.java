package com.sejong.sejongpeer.domain.image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sejong.sejongpeer.domain.image.entity.Image;
import com.sejong.sejongpeer.domain.image.entity.type.ImageFileExtension;
import com.sejong.sejongpeer.domain.image.entity.type.ImageType;

public interface ImageRepository extends JpaRepository<Image, Long> {
	@Query(
		"select i from Image i where i.imageType = :imageType and i.targetId = :targetId and i.imageFileExtension = :imageFileExtension order by i.id desc limit 1")
	Optional<Image> queryImageKey(
		ImageType imageType, Long targetId, ImageFileExtension imageFileExtension);

	List<Image> findAllByStudyId(Long studyId);
}
