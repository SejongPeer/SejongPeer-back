package com.sejong.sejongpeer.domain.college.api;

import com.sejong.sejongpeer.domain.college.dto.CollegeMajorResponse;
import com.sejong.sejongpeer.domain.college.service.CollegeMajorService;
import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "0. [학교]", description = "세종대 단과대 별 전공 정보 API")
@RestController
@RequestMapping("/api/v1/colleges")
@RequiredArgsConstructor
public class CollegeMajorController {

    private final CollegeMajorService collegeMajorService;

    @GetMapping
    public List<String> getAllDistinctColleges() {
        return collegeMajorService.getAllColleges();
    }

    @GetMapping("/majors")
    public List<CollegeMajorResponse> getMajorsByCollege(
            @RequestParam(name = "college") String college) {
        return collegeMajorService.getAllMajorsByCollege(college);
    }
}
