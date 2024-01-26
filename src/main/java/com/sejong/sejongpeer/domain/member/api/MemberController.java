package com.sejong.sejongpeer.domain.member.api;

import com.sejong.sejongpeer.domain.member.dto.SignUpRequest;
import com.sejong.sejongpeer.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. [회원]", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody SignUpRequest request) {
        memberService.signUp(request);

        return ResponseEntity.ok().build();
    }
}
