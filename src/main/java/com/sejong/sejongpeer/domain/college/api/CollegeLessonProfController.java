package com.sejong.sejongpeer.domain.college.api;

import com.sejong.sejongpeer.domain.college.dto.CollegeLessonProfResponse;
import com.sejong.sejongpeer.domain.college.service.CollegeLessonProfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "3. [스터디]", description = "스터디 관련 API입니다.")
@RestController
@RequestMapping("/api/v1/colleges")
@RequiredArgsConstructor
public class CollegeLessonProfController {

	private final CollegeLessonProfService lessonProfService;

	@Operation(summary = "수업 정보 조회", description = "스터디 게시글 작성에 필요한 단과대 별 개설된 수업 정보를 반환합니다.")
	@GetMapping("/lessons-info")
	public List<CollegeLessonProfResponse> getLessonInfoByColleage(
		@RequestParam(name = "college") String college) {
		return lessonProfService.getLessonInfoByColleage(college);
	}
}
