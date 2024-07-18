package com.sejong.sejongpeer.domain.study.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.entity.StudyTagMap;
import com.sejong.sejongpeer.domain.study.entity.Tag;
import com.sejong.sejongpeer.domain.study.repository.StudyRepository;
import com.sejong.sejongpeer.domain.study.repository.StudyTagMapRepository;
import com.sejong.sejongpeer.domain.study.repository.TagRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

	private final TagRepository tagRepository;
	private final StudyRepository studyRepository;
	private final StudyTagMapRepository studyTagMapRepository;

	public void setTagAndStudyTagMap(List<String> tags, Study study) {
		tags.forEach(tagName -> {
			String hashName = hashTagName(tagName);
			Tag tag = tagRepository.findByHashedName(hashName)
				.orElseGet(() -> tagRepository.save(new Tag(tagName)));

			StudyTagMap tagMap = StudyTagMap.builder()
				.study(study)
				.tag(tag)
				.build();

			tag.addStudyTagMap(tagMap);
			study.addStudyTagMap(tagMap);

			studyTagMapRepository.save(tagMap);
		});

		studyRepository.save(study);
	}

	private String hashTagName(String tagName) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(tagName.getBytes());
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new CustomException(ErrorCode.CANNOT_CHANGE_TO_HASHNAME);
		}
	}

	public List<String> getTagsNameByStudy(Study study) {
		Set<StudyTagMap> studyTagMaps = study.getStudyTagMaps();
		return studyTagMaps.stream()
			.map(studyTagMap -> studyTagMap.getTag().getName())
			.toList();
	}
}
