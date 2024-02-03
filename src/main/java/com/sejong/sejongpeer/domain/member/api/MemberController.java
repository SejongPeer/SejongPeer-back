package com.sejong.sejongpeer.domain.member.api;

import com.sejong.sejongpeer.domain.member.dto.request.AccountCheckRequest;
import com.sejong.sejongpeer.domain.member.dto.request.NicknameCheckRequest;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.dto.response.AccountCheckResponse;
import com.sejong.sejongpeer.domain.member.dto.response.MemberInfoResponse;
import com.sejong.sejongpeer.domain.member.dto.response.NicknameCheckResponse;
import com.sejong.sejongpeer.domain.member.service.MemberService;
import com.sejong.sejongpeer.security.MemberDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody SignUpRequest request) {
        memberService.signUp(request);
    }

    @Operation(summary = "아이디 중복 체크", description = "아이디 중복을 체크합니다.")
    @PostMapping("/check-account")
    public AccountCheckResponse checkAccount(@Valid @RequestBody AccountCheckRequest request) {
        if (memberService.isAccountExists(request.account())) {
            return new AccountCheckResponse(true);
        }
        return new AccountCheckResponse(false);
    }

    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크합니다.")
    @PostMapping("/check-nickname")
    public NicknameCheckResponse checkNickname(@Valid @RequestBody NicknameCheckRequest request) {
        if (memberService.isNicknameExists(request.nickname())) {
            return new NicknameCheckResponse(true);
        }
        return new NicknameCheckResponse(false);
    }

    @Operation(summary = "회원정보 조회", description = "회원정보를 조회합니다.")
    @GetMapping("/my-page")
    public MemberInfoResponse getMemberInfo() {
        MemberDetails memberDetails =
                (MemberDetails)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String memberId = memberDetails.getUsername();

        return memberService.getMemberInfo(memberId);
    }
}
