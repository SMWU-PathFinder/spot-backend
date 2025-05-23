package com.pathfinder.spot.common.auth;

import com.pathfinder.spot.common.exceptions.BaseException;
import com.pathfinder.spot.domain.member.Member;
import com.pathfinder.spot.domain.member.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JWT 토큰 제공 클래스 - 토큰 생성, 검증, 인증 정보 추출
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private final RedisTemplate<String, String> tokenRedisTemplate;
    private final MemberRepository memberRepository;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;

    @Value("${jwt.access.expiration}")
    private long accessExpirationTime;

    @Value("${jwt.refresh.expiration}")
    private long refreshExpirationTime;

    @Autowired
    public JwtTokenProvider(@Qualifier("tokenRedisTemplate") RedisTemplate<String, String> tokenRedisTemplate, MemberRepository memberRepository) {
        this.tokenRedisTemplate = tokenRedisTemplate;
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Member member) {
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("roles", List.of(member.getRole().name()));
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
    }
    public String createRefreshToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("type", "refresh"); // 토큰 타입을 명시적으로 추가
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();

        tokenRedisTemplate.opsForValue().set(
                email,
                refreshToken,
                refreshExpirationTime,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public boolean isAdmin(String token) {
        Claims claims = getClaimsFromToken(token);
        Object rolesObj = claims.get("roles");

        if (rolesObj instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) rolesObj;
            return roles.contains("ROLE_ADMIN");
        }
        return false;
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);
        String email = claims.getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found"));
        // JWT에서 roles 클레임 추출
        List<String> roles = claims.get("roles", List.class);
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new UsernamePasswordAuthenticationToken(member, null, authorities);
    }

    // 메소드 오버로딩
    public String resolveToken(HttpServletRequest httpServletRequest) { // HttpServletRequest를 인자로 받는 메소드
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveToken(String token) { // String을 인자로 받는 메소드
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired", e);
            throw e; // 예외를 던져 만료된 토큰 처리
        } catch (JwtException e) {
            log.error("Invalid JWT token", e);
            throw new BaseException("INVALID_JWT");
        }
    }

    // Refresh Token을 무효화하여 로그아웃 처리
    public void invalidateRefreshToken(String refreshToken) {
        try {
            Claims claims = getClaimsFromToken(refreshToken);
            String username = claims.getSubject();
            deleteRefreshToken(username, refreshToken);
        } catch (Exception e) {
            log.error("토큰 무효화 중 오류 발생: {}", e.getMessage());
            throw new BaseException("LOGOUT_ERROR");
        }
    }

    // Redis에서 토큰 삭제
    public void deleteRefreshToken(String username, String refreshToken) { // username과 refreshToken가 모두 일치하는 값 제거
        try {
            String storedToken = tokenRedisTemplate.opsForValue().get(username);
            if (refreshToken.equals(storedToken)) {
                tokenRedisTemplate.delete(username);
                log.info("Redis에서 토큰 삭제 완료: {}", username);
            } else {
                log.info("저장된 토큰과 일치하지 않음: {}", username);
            }
        } catch (Exception e) {
            log.error("Redis에서 토큰 삭제 중 오류 발생: {}", e.getMessage());
            throw new BaseException("DELETE_REDIS");
        }
    }

    // 블랙리스트에 refreshToken을 추가하고 만료 시간 설정
    public void addToBlacklist(String refreshToken) {
        long expiration = getClaimsFromToken(refreshToken).getExpiration().getTime();
        long currentTime = System.currentTimeMillis();
        long ttl = expiration - currentTime;

        tokenRedisTemplate.opsForValue().set(refreshToken, "invalid", ttl, TimeUnit.MILLISECONDS);
    }

    // 블랙리스트 확인하기
    public boolean isInBlacklist(String refreshToken) {
        return Boolean.TRUE.equals(tokenRedisTemplate.hasKey(refreshToken));
    }

    public String extractEmailFromToken(String token) {
        try {
            return getClaimsFromToken(token.replace("Bearer ", "")).getSubject();
        } catch (Exception e) {
            log.error("토큰에서 이메일 추출 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }

    public String createShortLivedTokenWithPurpose(Authentication authentication, String purpose) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("purpose", purpose);
        log.info("Creating token with purpose: {}", purpose);  // 로그 추가

        Date now = new Date();
        Date validity = new Date(now.getTime() + 600_000); // 매우 짧은 만료 시간인 10분을 가짐

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public boolean isRefreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return "refresh".equals(claims.get("type"));
    }
}