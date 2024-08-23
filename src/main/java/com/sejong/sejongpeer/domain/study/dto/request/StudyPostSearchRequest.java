package com.sejong.sejongpeer.domain.study.dto.request;

import io.micrometer.common.lang.Nullable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyPostSearchRequest {

	@Nullable
	@Schema(description = "원하는 스터디 모집 인원")
	Integer recruitmentPersonnel;

	@Nullable
	@Schema(description = "스터디 모집 여부에 대해서 모집 중은 true, 모집 마감은 false로 요청주세요.")
	Boolean isRecruiting;

	@Nullable
	@Schema(description = "검색어") String searchWord;

	@Nullable
	@Schema(description = "교내 게시글 검색의 경우 과목 id, 교외 게시글 검색의 경우에는 카테고리 id를 입력해주세요.")
	Long categoryId;

}
