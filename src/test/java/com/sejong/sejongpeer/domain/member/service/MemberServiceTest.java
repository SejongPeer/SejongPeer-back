package com.sejong.sejongpeer.domain.member.service;

import static org.mockito.BDDMockito.*;

import com.sejong.sejongpeer.config.PasswordEncoderTestConfig;
import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@Import(PasswordEncoderTestConfig.class)
class MemberServiceTest {
    @InjectMocks private MemberService memberService;

    @Mock
    private PasswordEncoder passwordEncoder = new PasswordEncoderTestConfig().passwordEncoder();

    @Mock private CollegeMajorRepository collegeMajorRepository;
    @Mock private MemberRepository memberRepository;

    private SignUpRequest signUpRequest;
    private String encryptedPassword;

    @BeforeEach
    void setUp() {
        this.encryptedPassword = passwordEncoder.encode("test");
        this.signUpRequest =
                new SignUpRequest(
                        "test",
                        "test",
                        "test",
                        "test",
                        "18011111",
                        "인문과학대학",
                        "국어국문학과",
                        "호텔관광대학",
                        "호텔관광외식경영학부",
                        2,
                        Gender.MALE,
                        LocalDate.of(1999, 1, 1),
                        "01011112222",
                        "tester",
                        "testkakao");
    }

    @Test
    void 정상_회원가입() {
        // given
        CollegeMajor major =
                CollegeMajor.builder()
                        .college(signUpRequest.college())
                        .major(signUpRequest.major())
                        .build();
        CollegeMajor subMajor =
                CollegeMajor.builder()
                        .college(signUpRequest.subCollege())
                        .major(signUpRequest.subMajor())
                        .build();
        given(
                        collegeMajorRepository.findByCollegeAndMajor(
                                signUpRequest.college(), signUpRequest.major()))
                .willReturn(Optional.ofNullable(major));
        given(
                        collegeMajorRepository.findByCollegeAndMajor(
                                signUpRequest.subCollege(), signUpRequest.subMajor()))
                .willReturn(Optional.ofNullable(subMajor));
        Member member = Member.create(signUpRequest, major, major, encryptedPassword);
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberRepository.findByAccount("test")).willReturn(Optional.ofNullable(member));

        // when
        Assertions.assertDoesNotThrow(() -> memberService.signUp(signUpRequest));
        Member savedMember = memberRepository.findByAccount("test").get();

        // then
        Assertions.assertEquals(member, savedMember);
        Assertions.assertEquals(member.getCollegeMajor(), savedMember.getCollegeMajor());
        Assertions.assertEquals(member.getCollegeMinor(), savedMember.getCollegeMinor());
    }

    @Test
    void 중복된_아이디_회원가입() {
        // given
        given(memberRepository.existsByAccount("test")).willReturn(true);

        // when
        CustomException e =
                Assertions.assertThrows(
                        CustomException.class, () -> memberService.signUp(signUpRequest));

        // then
        Assertions.assertEquals(ErrorCode.DUPLICATED_ACCOUNT, e.getErrorCode());
    }

    @Test
    void 중복된_학번_회원가입() {
        // given
        given(memberRepository.existsByStudentId(signUpRequest.studentId())).willReturn(true);

        // when
        CustomException e =
                Assertions.assertThrows(
                        CustomException.class, () -> memberService.signUp(signUpRequest));

        // then
        Assertions.assertEquals(ErrorCode.DUPLICATED_STUDENT_ID, e.getErrorCode());
    }

    @Test
    void 중복된_전화번호_회원가입() {
        // given
        given(memberRepository.existsByPhoneNumber("01011112222")).willReturn(true);

        // when
        CustomException e =
                Assertions.assertThrows(
                        CustomException.class, () -> memberService.signUp(signUpRequest));

        // then
        Assertions.assertEquals(ErrorCode.DUPLICATED_PHONE_NUMBER, e.getErrorCode());
    }

    @Test
    void 잘못된_비밀번호_재입력() {
        // given
        this.signUpRequest =
                new SignUpRequest(
                        "test",
                        "test",
                        "another",
                        "test",
                        "18011111",
                        "인문과학대학",
                        "국어국문학과",
                        "호텔관광대학",
                        "호텔관광외식경영학부",
                        2,
                        Gender.MALE,
                        LocalDate.of(1999, 1, 1),
                        "01011112222",
                        "tester",
                        "testkakao");

        // when
        CustomException e =
                Assertions.assertThrows(
                        CustomException.class, () -> memberService.signUp(signUpRequest));

        // then
        Assertions.assertEquals(ErrorCode.PASSWORD_NOT_MATCH, e.getErrorCode());
    }
}
