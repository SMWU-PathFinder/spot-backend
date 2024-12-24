package com.pathfinder.spot.application.member;

import com.pathfinder.spot.domain.member.Member;

public record LoginResponse(
        Long id,
        String nickname,
        String email,
        String grantType,
        String accessToken,
        String refreshToken
) {
    public static LoginResponse of(Member member, String accessToken, String refreshToken) {
        return new LoginResponse(
                member.getId(),
                member.getNickname(),
                member.getEmail(),
                "Bearer",
                accessToken,
                refreshToken
        );
    }
}
