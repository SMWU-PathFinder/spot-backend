package com.pathfinder.spot.application.member;

import com.pathfinder.spot.common.auth.JwtTokenProvider;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.member.MemberRepository;
import com.pathfinder.spot.domain.member.MemberType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pathfinder.spot.common.constants.ExceptionCode.FAIL_TO_SOCIAL_LOGIN;
import static com.pathfinder.spot.domain.member.SocialLoginType.KAKAO;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class MemberService {
    private final KakaoLoginService kakaoLoginService;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseEntity<?> login(String provider, String code) {
        if (provider.equals(KAKAO.getCode())) {
            LoginResponse loginResponse = getMemberInfo(kakaoLoginService.login(code));
            return ResponseEntity.ok(ApiResponse.success(loginResponse, "카카오 로그인 성공"));
        }
        return ResponseEntity.badRequest().body(ApiResponse.failure(String.valueOf(FAIL_TO_SOCIAL_LOGIN.getCode()), "지원하지 않는 소셜 로그인입니다."));
    }

    public LoginResponse getMemberInfo(OauthMember oauthMember) {
        Member member = findOrCreateMember(oauthMember);
        log.info("로그인한 사용자 정보: {}, {}, {}, {}, {}", member.getEmail(), member.getNickname(), member.getRole().name(), member.getSocialLoginType(), member.getSocialLoginId());

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());

        // Access Token에서 권한 정보 추출 및 Authentication 생성
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ROLE_ADMIN 여부 확인
        boolean isAdmin = jwtTokenProvider.isAdmin(accessToken);

        return LoginResponse.of(member, isAdmin, accessToken, refreshToken);
    }

    public Member findOrCreateMember(OauthMember oauthMember) {
        return memberRepository.findBySocialLoginId(oauthMember.socialLoginId())
                .orElseGet(() -> createMember(oauthMember));
    }

    public Member createMember(OauthMember oauthMember) {
        if (oauthMember.email() == null || oauthMember.email().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
        }

        Member member = Member.builder()
                .nickname(oauthMember.nickname())
                .email(oauthMember.email())
                .socialLoginType(oauthMember.socialLoginType())
                .socialLoginId(oauthMember.socialLoginId())
                .build();
        return memberRepository.save(member);
    }
}
