package com.pathfinder.spot.application.member;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // API 응답 데이터 snake_case 형태를 Java의 camelCase 형태로 매핑
public class KakaoMemberResponse {

    private Long id;
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {
        private String email;
        private KakaoProfile profile;
    }

    @Getter
    public static class KakaoProfile {
        private String nickname;
    }
}
