package com.pathfinder.spot.application.member;

import com.pathfinder.spot.domain.member.SocialLoginType;

public record OauthMember(SocialLoginType socialLoginType, String socialLoginId, String email, String nickname) {
    public static OauthMember of(SocialLoginType socialLoginType, String socialLoginId, String email, String nickname) {
        return new OauthMember(socialLoginType, socialLoginId, email, nickname);
    }
}
