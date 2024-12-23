package com.pathfinder.spot.domain.member;

import lombok.Getter;

@Getter
public enum SocialLoginType {

    KAKAO("kakao");

    private final String code;

    SocialLoginType(final String code) {
        this.code = code;
    }
}