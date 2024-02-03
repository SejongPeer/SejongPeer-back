package com.sejong.sejongpeer.domain.member.service;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.dto.response.MemberInfoResponse;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final CollegeMajorRepository collegeMajorRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        verifySignUp(request);

        Member member = createMember(request);
        memberRepository.save(member);

        log.info("회원가입 완료: {}", member);
    }

    private Member createMember(SignUpRequest request) {
        String encodedPassword = passwordEncoder.encode(request.password());

        CollegeMajor collegeMajor =
                collegeMajorRepository
                        .findByCollegeAndMajor(request.college(), request.major())
                        .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
        CollegeMajor subCollegeMajor = null;
        if (request.hasSubMajor()) {
            subCollegeMajor =
                    collegeMajorRepository
                            .findByCollegeAndMajor(request.subCollege(), request.subMajor())
                            .orElseThrow(
                                    () -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));
        }

        return Member.create(request, collegeMajor, subCollegeMajor, encodedPassword);
    }

    private void verifySignUp(SignUpRequest request) {
        if (!request.password().equals(request.passwordCheck())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        if (isPhoneNumberExists(request.phoneNumber())) {
            throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
        }
        if (isAccountExists(request.account())) {
            throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
        }
        if (isStudentIdExists(request.studentId())) {
            throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
        }
    }

    public MemberInfoResponse getMemberInfo(String memberId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        return MemberInfoResponse.of(member);
    }

    @Transactional(readOnly = true)
    public boolean isStudentIdExists(String studentId) {
        return memberRepository.existsByStudentId(studentId);
    }

    @Transactional(readOnly = true)
    public boolean isAccountExists(String account) {
        return memberRepository.existsByAccount(account);
    }

    @Transactional(readOnly = true)
    public boolean isPhoneNumberExists(String phoneNumber) {
        return memberRepository.existsByPhoneNumber(phoneNumber);
    }

    @Transactional(readOnly = true)
    public boolean isNicknameExists(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
