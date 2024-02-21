package com.sejong.sejongpeer.domain.member.service;

import static org.mockito.BDDMockito.*;

import com.sejong.sejongpeer.config.PasswordEncoderTestConfig;
import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.domain.college.repository.CollegeMajorRepository;
import com.sejong.sejongpeer.domain.member.dto.request.MemberUpdateRequest;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.dto.response.MemberInfoResponse;
import com.sejong.sejongpeer.domain.member.entity.Member;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.repository.MemberRepository;
import com.sejong.sejongpeer.global.error.exception.CustomException;
import com.sejong.sejongpeer.global.error.exception.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@Import(PasswordEncoderTestConfig.class)
class MemberServiceTest {
    @InjectMocks private MemberService memberService;
    @Spy private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Mock private CollegeMajorRepository collegeMajorRepository;
    @Mock private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원가입 테스트")
    class SignUpTest {
        private static final String MEMBER_PASSWORD = "password";
        private SignUpRequest signUpRequest;

        @BeforeEach
        void setUp() {
            signUpRequest =
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
            CollegeMajor minor =
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
                    .willReturn(Optional.ofNullable(minor));

            Member member =
                    Member.create(
                            signUpRequest, major, minor, passwordEncoder.encode(MEMBER_PASSWORD));
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
            signUpRequest =
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

    @Nested
    @DisplayName("회원정보 조회 및 수정 테스트")
    class GetMemberInfoTest {
        private static final String MEMBER_ID = "test-id";
        private static final String MAJOR_COLLEGE_NAME = "Major College";
        private static final String MAJOR_NAME = "Major";
        private static final String MINOR_COLLEGE_NAME = "Minor College";
        private static final String MINOR_NAME = "Minor";
        private static final String MEMBER_PASSWORD = "password";

        private Member member;
        private String encryptedPassword;

        @BeforeEach
        void setUp() {
            encryptedPassword = passwordEncoder.encode(MEMBER_PASSWORD);

            CollegeMajor collegeMajor =
                    CollegeMajor.builder().college(MAJOR_COLLEGE_NAME).major(MAJOR_NAME).build();
            CollegeMajor collegeMinor =
                    CollegeMajor.builder().college(MINOR_COLLEGE_NAME).major(MINOR_NAME).build();
            member =
                    Member.builder()
                            .phoneNumber("01012341234")
                            .grade(1)
                            .gender(Gender.MALE)
                            .nickname("tester")
                            .studentId("12345678")
                            .name("홍길동")
                            .kakaoAccount("test")
                            .collegeMajor(collegeMajor)
                            .collegeMinor(collegeMinor)
                            .account("test")
                            .password(encryptedPassword)
                            .build();
        }

        @Test
        void 회원정보_조회() {
            // given
            given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.ofNullable(member));

            // when
            MemberInfoResponse memberInfo = memberService.getMemberInfo(MEMBER_ID);

            // then
            Assertions.assertEquals(member.getName(), memberInfo.name());
            Assertions.assertEquals(member.getAccount(), memberInfo.account());
            Assertions.assertEquals(member.getCollegeMajor().getMajor(), memberInfo.major());
            Assertions.assertEquals(member.getCollegeMinor().getMajor(), memberInfo.minor());
            Assertions.assertEquals(member.getNickname(), memberInfo.nickname());
            Assertions.assertEquals(member.getPhoneNumber(), memberInfo.phoneNumber());
            Assertions.assertEquals(member.getStudentId(), memberInfo.studentId());
            Assertions.assertEquals(member.getGender(), memberInfo.gender());
        }

        @Test
        void 닉네임_변경() {
            // given
            String newNickname = "newNickname";
            MemberUpdateRequest request =
                    new MemberUpdateRequest(newNickname, null, null, MEMBER_PASSWORD, null, null);

            given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.ofNullable(member));

            // when
            memberService.updateMemberInfo(MEMBER_ID, request);

            // then
            Assertions.assertEquals(newNickname, member.getNickname());
        }

        @Test
        void 비밀번호_변경() {
            // given
            String newPassword = "newPassword";
            MemberUpdateRequest request =
                    new MemberUpdateRequest(
                            null, null, null, MEMBER_PASSWORD, newPassword, newPassword);

            given(memberRepository.findById(MEMBER_ID)).willReturn(Optional.ofNullable(member));

            // when
            memberService.updateMemberInfo(MEMBER_ID, request);

            // then
            Assertions.assertTrue(passwordEncoder.matches(newPassword, member.getPassword()));
        }
    }
}
