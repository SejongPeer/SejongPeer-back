package com.sejong.sejongpeer.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sejong.sejongpeer.domain.auth.entity.RefreshToken;
import com.sejong.sejongpeer.domain.auth.repository.RefreshTokenRepository;
import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;
import com.sejong.sejongpeer.domain.member.dto.request.AccountFindRequest;
import com.sejong.sejongpeer.domain.member.dto.request.MemberUpdateRequest;
import com.sejong.sejongpeer.domain.member.dto.request.PasswordResetRequest;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.dto.response.AccountFindResponse;
import com.sejong.sejongpeer.domain.member.dto.response.ExistsCheckResponse;
import com.sejong.sejongpeer.domain.member.dto.response.MemberInfoResponse;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.MemberInfo;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import com.sejong.sejongpeer.global.util.MemberUtil;
import com.sejong.sejongpeer.global.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class MemberService {
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final CollegeMajorRepository collegeMajorRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberUtil memberUtil;
	private final SecurityUtil securityUtil;

	public void signUp(SignUpRequest request) {
		verifySignUp(request);

		Member member = createMember(request);
		memberRepository.save(member);

		log.info("회원가입 완료: {}", member);
	}

	private void verifyStudentId(String studentId) {
		if (existsStudentId(studentId)) {
			throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
		}
	}

	private void verifyAccount(String account) {
		if (existsAccount(account)) {
			throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
		}
	}

	private void verifyPhoneNumber(String phoneNumber) {
		if (existsPhoneNumber(phoneNumber)) {
			throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
		}
	}

	private void verifyPassword(String password, String passwordCheck) {
		if (!password.equals(passwordCheck)) {
			throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
		}
	}

	@Transactional(readOnly = true)
	public MemberInfoResponse getMemberInfo() {
		final Member currentMember = memberUtil.getCurrentMember();
		return MemberInfoResponse.from(currentMember);
	}

	private boolean existsStudentId(String studentId) {
		return memberRepository.existsByStudentId(studentId);
	}

	private boolean existsAccount(String account) {
		return memberRepository.existsByAccount(account);
	}

	private boolean existsPhoneNumber(String phoneNumber) {
		return memberRepository.existsByPhoneNumber(phoneNumber);
	}

	private boolean existsNickname(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}

	private boolean existsKakaoAccount(String kakaoAccount) {
		return memberRepository.existsByKakaoAccount(kakaoAccount);
	}

	public void updateMemberInfo(MemberUpdateRequest request) {
		final Member member = memberUtil.getCurrentMember();

		updateMember(member, request);
	}

	private void updateMember(Member member, MemberUpdateRequest request) {
		verifyUpdatable(request);
		MemberInfo.NICKNAME.executeUpdate(member, request.nickname());
		MemberInfo.PHONE_NUMBER.executeUpdate(member, request.phoneNumber());
		MemberInfo.KAKAO_ACCOUNT.executeUpdate(member, request.kakaoAccount());

		log.info("회원정보 변경 완료: {}", member.getId());
	}

	// 원자성 보장을 위해 하나라도 잘못되거나 중복된 정보가 있으면 업데이트 되어서는 안됨
	private void verifyUpdatable(MemberUpdateRequest request) {
		if (request.nickname() != null && existsNickname(request.nickname())) {
			throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
		}

		if (request.phoneNumber() != null && existsPhoneNumber(request.phoneNumber())) {
			throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
		}

		if (request.kakaoAccount() != null && existsKakaoAccount(request.kakaoAccount())) {
			throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ACCOUNT);
		}
	}

	@Transactional(readOnly = true)
	public AccountFindResponse findMemberAccount(AccountFindRequest request) {
		Member member = memberRepository.findByStudentIdAndName(request.studentId(), request.name())
			.orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

		return AccountFindResponse.of(member);
	}

	public void resetPassword(PasswordResetRequest request) {
		Member member =
			memberRepository
				.findByAccountAndStudentId(request.account(), request.studentId())
				.orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

		if (!request.password().equals(request.passwordCheck())) {
			throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
		}

		member.changePassword(passwordEncoder.encode(request.password()));
	}

	public void deleteMember() {
		final String memberId = securityUtil.getCurrentMemberId();
		RefreshToken refreshToken = refreshTokenRepository.findByMemberId(memberId)
			.orElseThrow(() -> new CustomException(
				ErrorCode.UNAUTHORIZED));    // refresh token이 없다는 것은 로그인하지 않았단 뜻. 로그인 없이 회원탈퇴는 불가

		refreshTokenRepository.delete(refreshToken);

		Member member =
			memberRepository
				.findById(memberId)
				.orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

		memberRepository.delete(member);
	}

	@Transactional(readOnly = true)
	public ExistsCheckResponse checkAccountExists(String account) {
		return ExistsCheckResponse.of(memberRepository.existsByAccount(account));
	}

	@Transactional(readOnly = true)
	public ExistsCheckResponse checkNicknameExists(String nickname) {
		return ExistsCheckResponse.of(memberRepository.existsByNickname(nickname));
	}

	@Transactional(readOnly = true)
	public ExistsCheckResponse checkPhoneNumberExists(String phoneNumber) {
		return ExistsCheckResponse.of(memberRepository.existsByPhoneNumber(phoneNumber));
	}

	@Transactional(readOnly = true)
	public ExistsCheckResponse checkKakaoAccountExists(String kakaoAccount) {
		return ExistsCheckResponse.of(memberRepository.existsByKakaoAccount(kakaoAccount));
	}

	private Member createMember(SignUpRequest request) {
		String encodedPassword = passwordEncoder.encode(request.password());

		CollegeMajor collegeMajor =
			collegeMajorRepository
				.findByCollegeAndMajor(request.college(), request.major())
				.orElseThrow(() -> new CustomException(ErrorCode.COLLEGE_NOT_FOUND));

		CollegeMajor collegeMinor = null;
		if (request.hasSubMajor()) {
			collegeMinor =
				collegeMajorRepository
					.findByCollegeAndMajor(request.subCollege(), request.subMajor())
					.orElseThrow(() -> new CustomException(ErrorCode.COLLEGE_NOT_FOUND));
		}

		return Member.create(request, collegeMajor, collegeMinor, encodedPassword);
	}

	private void verifySignUp(SignUpRequest request) {
		verifyPassword(request.password(), request.passwordCheck());
		verifyPhoneNumber(request.phoneNumber());
		verifyAccount(request.account());
		verifyStudentId(request.studentId());
		verifyNickname(request.nickname());
		verifyKakaoAccount(request.kakaoAccount());
	}

	private void verifyKakaoAccount(String kakaoAccount) {
		if (existsKakaoAccount(kakaoAccount)) {
			throw new CustomException(ErrorCode.DUPLICATED_KAKAO_ACCOUNT);
		}
	}

	private void verifyNickname(String nickname) {
		if (existsNickname(nickname)) {
			throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
		}
	}
}
