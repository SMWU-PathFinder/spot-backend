package com.pathfinder.spot.application.member;

import com.pathfinder.spot.common.auth.JwtTokenProvider;
import com.pathfinder.spot.common.dto.ApiResponse;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.pathfinder.spot.common.constants.ExceptionCode.FAIL_TO_SOCIAL_LOGIN;
import static com.pathfinder.spot.domain.member.SocialLoginType.KAKAO;

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

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // 기본 권한: ROLE_USER
        Authentication authentication = new UsernamePasswordAuthenticationToken( // 인증된 사용자 정보를 담는 객체를 생성 (이메일과 "ROLE_USER" 권한을 포함)
                member.getEmail(), "", authorities); // 빈 문자열로 비밀번호 설정

        // 인증된 사용자 정보를 SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        return LoginResponse.of(member, accessToken, refreshToken);
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
