package com.sejong.sejongpeer.domain.member.service;

import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;
import com.sejong.sejongpeer.domain.member.dto.request.AccountFindRequest;
import com.sejong.sejongpeer.domain.member.dto.request.MemberUpdateRequest;
import com.sejong.sejongpeer.domain.member.dto.request.PasswordResetRequest;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.dto.response.AccountCheckResponse;
import com.sejong.sejongpeer.domain.member.dto.response.AccountFindResponse;
import com.sejong.sejongpeer.domain.member.dto.response.MemberInfoResponse;
import com.sejong.sejongpeer.domain.member.dto.response.NicknameCheckResponse;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.domain.member.type.AccountFindOption;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
        if (!request.password().equals(request.passwordCheck())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        if (existsPhoneNumber(request.phoneNumber())) {
            throw new CustomException(ErrorCode.DUPLICATED_PHONE_NUMBER);
        }
        if (existsAccount(request.account())) {
            throw new CustomException(ErrorCode.DUPLICATED_ACCOUNT);
        }
        if (existsStudentId(request.studentId())) {
            throw new CustomException(ErrorCode.DUPLICATED_STUDENT_ID);
        }
        if (existsNickname(request.nickname())) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(String memberId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberInfoResponse.of(member);
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

    public void updateMemberInfo(String memberId, MemberUpdateRequest request) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.INTERNAL_SERVER_ERROR));

        if (!passwordEncoder.matches(request.currentPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_NOT_MATCH);
        }

        if (request.nickname() != null) {
            member.changeNickname(request.nickname());
        }

        if (request.newPassword() != null
                && (request.newPassword()).equals(request.newPasswordCheck())) {
            String encryptedPassword = passwordEncoder.encode(request.newPassword());
            member.changePassword(encryptedPassword);
        }
    }

    @Transactional(readOnly = true)
    public AccountFindResponse findMemberAccount(AccountFindRequest request) {
        Map<AccountFindOption, Function<AccountFindRequest, String>> findAccountByOption =
                Map.of(
                        AccountFindOption.PHONE_NUMBER, this::findMemberAccountByPhoneNumber,
                        AccountFindOption.NAME, this::findMemberAccountByName);

        String account =
                Optional.ofNullable(findAccountByOption.get(request.option()))
                        .orElseThrow(
                                () -> new CustomException(ErrorCode.ACCOUNT_FIND_OPTION_NOT_FOUND))
                        .apply(request);

        return new AccountFindResponse(account);
    }

    private String findMemberAccountByName(AccountFindRequest request) {
        Member member =
                memberRepository
                        .findByNameAndStudentId(request.name(), request.studentId())
                        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        return member.getAccount();
    }

    private String findMemberAccountByPhoneNumber(AccountFindRequest request) {
        Member member =
                memberRepository
                        .findByPhoneNumberAndStudentId(request.phoneNumber(), request.studentId())
                        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        return member.getAccount();
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

    public void deleteMember(String memberId) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public AccountCheckResponse checkAccountExists(String account) {
        return new AccountCheckResponse(memberRepository.existsByAccount(account));
    }

    @Transactional(readOnly = true)
    public NicknameCheckResponse checkNicknameExists(String nickname) {
        return new NicknameCheckResponse(memberRepository.existsByNickname(nickname));
    }
}
