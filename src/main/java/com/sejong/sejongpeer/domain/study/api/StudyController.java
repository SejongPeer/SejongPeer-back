package com.sejong.sejongpeer.domain.study.api;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.study.dto.request.StudyCreateRequest;
import com.sejong.sejongpeer.domain.study.dto.response.StudyCreateResponse;
import com.sejong.sejongpeer.domain.study.dto.response.StudyFindResponse;
import com.sejong.sejongpeer.domain.study.entity.Study;
import com.sejong.sejongpeer.domain.study.service.StudyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "1. [스터디]", description = "스터디 관련 API입니다.")
@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {

	private final StudyService studyService;

	@Operation(summary = "스터디 생성", description = "스터디를 생성합니다.")
	@PostMapping
	public ResponseEntity<StudyCreateResponse> studyCreate(@Valid @RequestBody StudyCreateRequest studyCreateRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(studyService.createStudy(studyCreateRequest));
	}

	@Operation(summary = "스터디 단건 조회", description = "스터디 한개를 조회합니다.")
	@GetMapping("/{studyId}")
	public StudyFindResponse studyFindOne(@PathVariable Long studyId) {
		return studyService.findOneStudy(studyId);
	}

	@Operation(summary = "스터디 리스트 조회", description = "스터디 리스트를 조회합니다.")
	@GetMapping
	public Slice<StudyFindResponse> studyFindSlice(@RequestParam("size") int size, @RequestParam("lastId") Long lastId) {
		return studyService.findSliceStudy(size, lastId);
	}
}
