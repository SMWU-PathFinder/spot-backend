package com.pathfinder.spot.application.member;

import com.pathfinder.spot.config.KakaoLoginApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pathfinder.spot.domain.member.SocialLoginType.KAKAO;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KakaoLoginService {

    private final KakaoLoginApiClient kakaoLoginApiClient;

    public OauthMember login(final String accessToken) {
        KakaoMemberResponse kakaoMemberResponse = kakaoLoginApiClient.getKakaoMemberInfo("Bearer " + accessToken);
        String socialLoginId = kakaoMemberResponse.getId().toString();
        String email = kakaoMemberResponse.getKakaoAccount().getEmail() != null
                ? kakaoMemberResponse.getKakaoAccount().getEmail()
                : "No Email"; // 이메일이 없을 경우 "No Email"으로 설정
        String nickname = kakaoMemberResponse.getKakaoAccount().getProfile() != null
                ? kakaoMemberResponse.getKakaoAccount().getProfile().getNickname()
                : "No Nickname"; // 닉네임이 없을 경우 "No Nickname"으로 설정

        log.info("socialLoginId: {}, email: {}, nickname: {}", socialLoginId, email, nickname);
        return OauthMember.of(
                KAKAO,
                socialLoginId,
                email,
                nickname
        );
    }
}
