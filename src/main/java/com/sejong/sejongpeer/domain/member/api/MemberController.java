package com.sejong.sejongpeer.domain.member.api;

import com.sejong.sejongpeer.domain.member.dto.SignUpRequest;
import com.sejong.sejongpeer.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody SignUpRequest request) {
        memberService.signUp(request);

        return ResponseEntity.ok().build();
    }
}
