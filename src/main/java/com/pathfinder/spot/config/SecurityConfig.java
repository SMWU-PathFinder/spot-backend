package com.pathfinder.spot.config;

import com.pathfinder.spot.common.auth.JwtFilter;
import com.pathfinder.spot.common.auth.JwtTokenProvider;
import com.pathfinder.spot.common.exceptions.JwtAccessDeniedHandler;
import com.pathfinder.spot.common.exceptions.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
    };
    private static final String[] ADMIN_WHITELIST = {
            "/api/v1/admin/**",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF 비활성화
                .csrf(csrf -> csrf.disable())
                // Stateless 세션 세션 관리 정책 설정
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 권한 요청 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll() // 인증 없이 누구나 접근 가능
                        .requestMatchers(ADMIN_WHITELIST).hasRole("ADMIN") // 관리자만 접근 가능
                        .anyRequest().authenticated() // 나머지는 인증된 사용자만 접근 가능
                )
                // 예외 처리
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(jwtAccessDeniedHandler) // 접근 거부 처리기 설정
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // 인증 진입점 설정
                );
        // JWT 필터 추가
        http.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedOriginPatterns(List.of("*")); // 모든 Origin 허용
        configuration.setAllowedHeaders(List.of("*")); // 모든 Header 허용
        configuration.setExposedHeaders(List.of("*")); // 모든 Header 노출
        configuration.setAllowCredentials(true); // 쿠키 및 인증정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
