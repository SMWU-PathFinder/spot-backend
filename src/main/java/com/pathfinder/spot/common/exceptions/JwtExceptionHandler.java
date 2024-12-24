package com.pathfinder.spot.common.exceptions;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtExceptionHandler {
    public void handleJwtException(HttpServletResponse response, Exception e) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


        if (e instanceof ExpiredJwtException expiredException) { // 만료된 토큰
            boolean isRefreshToken = false;
            try {
                Claims claims = expiredException.getClaims();
                if (claims != null) {
                    isRefreshToken = "refresh".equals(claims.get("type"));
                }
            } catch (Exception ex) {
                log.error("Error while checking if token is refresh token", ex);
            }
            if (isRefreshToken) {
                response.getWriter().write("{\"error\": \"refresh_token_expired\", \"message\": \"Refresh Token이 만료되었습니다.\"}");
            } else {
                response.getWriter().write("{\"error\": \"access_token_expired\", \"message\": \"Access Token이 만료되었습니다.\"}");
            }
        } else { // 유효하지 않은 토큰
            response.getWriter().write("{\"error\": \"invalid_token\", \"message\": \"유효하지 않은 Access Token입니다.\"}");
        }
    }
}
