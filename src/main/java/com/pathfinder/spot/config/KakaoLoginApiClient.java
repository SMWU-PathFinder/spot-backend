package com.pathfinder.spot.config;

import com.pathfinder.spot.application.member.KakaoMemberResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface KakaoLoginApiClient {
    @GetExchange("/v2/user/me")
    KakaoMemberResponse getKakaoMemberInfo(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}
