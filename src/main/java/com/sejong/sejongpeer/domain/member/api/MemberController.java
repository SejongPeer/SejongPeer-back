package com.sejong.sejongpeer.domain.member.api;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sejong.sejongpeer.domain.member.dto.request.AccountFindRequest;
import com.sejong.sejongpeer.domain.member.dto.request.MemberUpdateRequest;
import com.sejong.sejongpeer.domain.member.dto.request.PasswordResetRequest;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.dto.response.AccountFindResponse;
import com.sejong.sejongpeer.domain.member.dto.response.ExistsCheckResponse;
import com.sejong.sejongpeer.domain.member.dto.response.MemberInfoResponse;
import com.sejong.sejongpeer.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@Tag(name = "2. [회원]", description = "회원 관련 API")
@Validated
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
	@GetMapping("/check-account")
	public ExistsCheckResponse checkAccount(
		@RequestParam
		@NotBlank
		@Pattern(
			regexp = "^[a-zA-Z0-9]{4,24}$",
			message = "최소 4자, 최대 24자 영문과 숫자로만 이루어져야합니다.")
		String account) {

		return memberService.checkAccountExists(account);
	}

	@Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크합니다.")
	@GetMapping("/check-nickname")
	public ExistsCheckResponse checkNickname(
		@RequestParam
		@NotBlank(message = "닉네임을 입력해주세요.")
		@Pattern(
			regexp = "^[a-zA-Z가-힣ㄱ-ㅎㅏ-ㅣ0-9]{2,8}$",
			message = "닉네임은 2자 이상 8자 이하 한글, 영어, 숫자만 입력해주세요.")
		String nickname) {
		return memberService.checkNicknameExists(nickname);
	}

	@Operation(summary = "휴대폰 번호 중복 체크", description = "휴대폰 번호 중복을 체크합니다.")
	@GetMapping("/check-phone-number")
	public ExistsCheckResponse checkPhoneNumber(
		@RequestParam
		@NotBlank(message = "휴대폰 번호를 입력해주세요.")
		@Pattern(regexp = "^010[0-9]{8}$", message = "휴대폰 번호를 정확히 입력해주세요.")
		String phoneNumber) {
		return memberService.checkPhoneNumberExists(phoneNumber);
	}

	@Operation(summary = "카카오 계정 중복 체크", description = "카카오 계정 중복을 체크합니다.")
	@GetMapping("/check-kakao-account")
	public ExistsCheckResponse checkKakaoAccount(
		@RequestParam @NotBlank(message = "카카오 계정을 입력해주세요.") String kakaoAccount) {
		return memberService.checkKakaoAccountExists(kakaoAccount);
	}

	@Operation(summary = "회원정보 조회", description = "회원정보를 조회합니다.")
	@GetMapping("/my-page")
	public MemberInfoResponse getMemberInfo() {
		return memberService.getMemberInfo();
	}

	@Operation(summary = "회원정보 수정", description = "회원정보를 수정합니다.")
	@PatchMapping("/my-page")
	public void updateMemberInfo(@Valid @RequestBody MemberUpdateRequest request) {
		// String memberId = SecurityContextUtil.extractMemberId();

		memberService.updateMemberInfo(request);
	}

	@Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
	@DeleteMapping("/my-page")
	public void deleteMember() {
		// String memberId = SecurityContextUtil.extractMemberId();

		memberService.deleteMember();
	}

	@Operation(summary = "아이디 찾기", description = "학번과 이름 및 전화번호로 아이디를 찾습니다.")
	@PostMapping("/help/find-account")
	public AccountFindResponse findMemberAccount(@Valid @RequestBody AccountFindRequest request) {
		return memberService.findMemberAccount(request);
	}

	@Operation(summary = "비밀번호 초기화", description = "PW 찾기 시, 학번과 아이디로 비밀번호를 변경합니다.")
	@PutMapping("/help/reset-password")
	public void resetPassword(@Valid @RequestBody PasswordResetRequest request) {
		memberService.resetPassword(request);
	}
}
