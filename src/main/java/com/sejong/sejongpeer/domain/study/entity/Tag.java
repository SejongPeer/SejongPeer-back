package com.sejong.sejongpeer.domain.study.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Comment("태그 이름")
	private String name;

	@Column(nullable = false)
	@Comment("태그 해시값")
	private String hashedName;

	@OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<StudyTagMap> studyTagMaps = new HashSet<>();

	public Tag(String name) {
		this.name = name;
		this.hashedName = hashTagName(name);
	}

	private String hashTagName(String tagName) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(tagName.getBytes());
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Error hashing tag name", e);
		}
	}

	public void addStudyTagMap(StudyTagMap tagMap) {
		this.studyTagMaps.add(tagMap);
		tagMap.setTag(this);
	}
}
