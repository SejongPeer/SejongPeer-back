package com.sejong.sejongpeer.domain.college.api;

import com.sejong.sejongpeer.domain.college.dto.CollegeMajorResponse;
import com.sejong.sejongpeer.domain.college.service.CollegeMajorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
