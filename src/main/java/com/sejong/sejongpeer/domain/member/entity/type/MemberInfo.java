package com.sejong.sejongpeer.domain.member.entity.type;

import com.sejong.sejongpeer.domain.member.dto.request.MemberUpdateRequest;
import com.sejong.sejongpeer.domain.member.entity.Member;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// TODO: entity.type 패키지에 들어있어야 하는게 맞는지? 적절한 enum명인지?
@RequiredArgsConstructor
@Getter
public enum MemberInfo {
    // PASSWORD(request -> request.newPassword()) {
    //     @Override
    //     protected void update(Member member, String value) {
    //         member.changePassword(value);
    //     }
    // },
    NICKNAME(request -> request.nickname()) {
        @Override
        protected void update(Member member, String value) {
            member.changeNickname(value);
        }
    },
    PHONE_NUMBER(request -> request.phoneNumber()) {
        @Override
        protected void update(Member member, String value) {
            member.changePhoneNumber(value);
        }
    },
    KAKAO_ACCOUNT(request -> request.kakaoAccount()) {
        @Override
        protected void update(Member member, String value) {
            member.changeKakaoAccount(value);
        }
    };

    // 추후를 위해 executeUpdateAll을 위해 일단 만들어둠
    private final Function<MemberUpdateRequest, String> valueExtractor;

    protected abstract void update(Member member, String value);

    public void executeUpdate(Member member, String value) {
        if (value != null) {
            update(member, value);
        }
    }

    public static void executeUpdateAll(Member member, MemberUpdateRequest request) {
        for (MemberInfo memberInfo : values()) {
            memberInfo.executeUpdate(member, memberInfo.valueExtractor.apply(request));
        }
    }
}
