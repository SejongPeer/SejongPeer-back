package com.sejong.sejongpeer.domain.member.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sejong.sejongpeer.domain.member.dto.request.SignUpRequest;
import com.sejong.sejongpeer.domain.member.entity.type.Gender;
import com.sejong.sejongpeer.domain.member.service.MemberService;
import com.sejong.sejongpeer.security.util.JwtProvider;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockBean({JpaMetamodelMappingContext.class})
class MemberControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MemberService memberService;
	@MockBean
	private JwtProvider jwtProvider;

	@Nested
	class 회원가입 {
		private static String DEFAULT_ACCOUNT = "test";
		private static String DEFAULT_PASSWORD = "test1q2w34r!";
		private static String DEFAULT_NAME = "test";
		private static String DEFAULT_STUDENT_ID = "test";
		private static Gender DEFAULT_GENDER = Gender.FEMALE;
		private static String DEFAULT_MAJOR = "test";
		private static String DEFAULT_COLLEGE = "test";
		private static int DEFAULT_GRADE = 1;
		private static String DEFAULT_NICKNAME = "닉네ㅔ임";
		private static String DEFAULT_PHONE_NUMBER = "01012345678";
		private static String DEFAULT_KAKAO_ACCOUNT = "test";

		private static String MATHOD_ARGUMENT_NOT_VALID_EXCEPTION = "MethodArgumentNotValidException";

		@Test
		void 정상_회원가입_요청() throws Exception {
			// Given
			SignUpRequest request = SignUpRequest.builder()
				.account(DEFAULT_ACCOUNT)
				.password(DEFAULT_PASSWORD)
				.passwordCheck(DEFAULT_PASSWORD)
				.name(DEFAULT_NAME)
				.studentId(DEFAULT_STUDENT_ID)
				.gender(DEFAULT_GENDER)
				.major(DEFAULT_MAJOR)
				.college(DEFAULT_COLLEGE)
				.grade(DEFAULT_GRADE)
				.nickname(DEFAULT_NICKNAME)
				.phoneNumber(DEFAULT_PHONE_NUMBER)
				.kakaoAccount(DEFAULT_KAKAO_ACCOUNT)
				.build();

			// When
			ResultActions resultActions = mockMvc.perform(post("/api/v1/member/sign-up")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			);

			// Then
			resultActions
				.andExpect(status().isOk());
		}

		@Test
		void 잘못된_닉네임_요청() throws Exception {
			// Given
			String WRONG_NICKNAME = "t";

			SignUpRequest request = SignUpRequest.builder()
				.account(DEFAULT_ACCOUNT)
				.password(DEFAULT_PASSWORD)
				.passwordCheck(DEFAULT_PASSWORD)
				.name(DEFAULT_NAME)
				.studentId(DEFAULT_STUDENT_ID)
				.gender(DEFAULT_GENDER)
				.major(DEFAULT_MAJOR)
				.college(DEFAULT_COLLEGE)
				.grade(DEFAULT_GRADE)
				.nickname(WRONG_NICKNAME)
				.phoneNumber(DEFAULT_PHONE_NUMBER)
				.kakaoAccount(DEFAULT_KAKAO_ACCOUNT)
				.build();

			// When
			ResultActions resultActions = mockMvc.perform(post("/api/v1/member/sign-up")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			);

			// Then
			resultActions
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data.errorClassName").value(MATHOD_ARGUMENT_NOT_VALID_EXCEPTION));
		}

		@Test
		void 잘못된_전화번호_요청() throws Exception {
			// Given
			String WRONG_PHONE_NUMBER = "0101234567";
			SignUpRequest request = SignUpRequest.builder()
				.account(DEFAULT_ACCOUNT)
				.password(DEFAULT_PASSWORD)
				.passwordCheck(DEFAULT_PASSWORD)
				.name(DEFAULT_NAME)
				.studentId(DEFAULT_STUDENT_ID)
				.gender(DEFAULT_GENDER)
				.major(DEFAULT_MAJOR)
				.college(DEFAULT_COLLEGE)
				.grade(DEFAULT_GRADE)
				.nickname(DEFAULT_NICKNAME)
				.phoneNumber(WRONG_PHONE_NUMBER)
				.kakaoAccount(DEFAULT_KAKAO_ACCOUNT)
				.build();

			// When
			ResultActions resultActions = mockMvc.perform(post("/api/v1/member/sign-up")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			);

			// Then
			resultActions
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data.errorClassName").value(MATHOD_ARGUMENT_NOT_VALID_EXCEPTION));
		}

		@Test
		void 잘못된_비밀번호_형식() throws Exception {
			// Given
			String WRONG_PASSWORD = "test1";
			SignUpRequest request = SignUpRequest.builder()
				.account(DEFAULT_ACCOUNT)
				.password(WRONG_PASSWORD)
				.passwordCheck(WRONG_PASSWORD)
				.name(DEFAULT_NAME)
				.studentId(DEFAULT_STUDENT_ID)
				.gender(DEFAULT_GENDER)
				.major(DEFAULT_MAJOR)
				.college(DEFAULT_COLLEGE)
				.grade(DEFAULT_GRADE)
				.nickname(DEFAULT_NICKNAME)
				.phoneNumber(DEFAULT_PHONE_NUMBER)
				.kakaoAccount(DEFAULT_KAKAO_ACCOUNT)
				.build();

			// When
			ResultActions resultActions = mockMvc.perform(post("/api/v1/member/sign-up")
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON)
			);

			// Then
			resultActions
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data.errorClassName").value(MATHOD_ARGUMENT_NOT_VALID_EXCEPTION));
		}

	}

	@Test
	void checkNickname() {
	}

	@Test
	void checkPhoneNumber() {
	}

	@Test
	void checkKakaoAccount() {
	}

	@Test
	void getMemberInfo() {
	}

	@Test
	void updateMemberInfo() {
	}

	@Test
	void deleteMember() {
	}

	@Test
	void findMemberAccount() {
	}

	@Test
	void resetPassword() {
	}
}
