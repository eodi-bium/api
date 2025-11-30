package com.eodi.bium.member.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eodi.bium.member.properties.JwtProperties;
import com.eodi.bium.member.security.SecurityConfig.TokenRotationService;
import com.eodi.bium.member.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class RefreshRotationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenRotationService tokenRotationService;
    // private final MemberRepository memberRepository; // [제거] 더 이상 필요 없음

    private boolean skip(String path) {
        return path.startsWith("/oauth2") || path.startsWith("/place") || path.equals(
            "/member/refresh");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        if (skip(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        boolean isAccessTokenValid = false;

        // 1. Access Token 검증 시도
        if (auth != null && auth.startsWith(BEARER_PREFIX)) {
            String at = auth.substring(BEARER_PREFIX.length());
            try {
                JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(at);
                isAccessTokenValid = true;
            } catch (Exception e) {
                isAccessTokenValid = false;
            }
        }

        if (isAccessTokenValid) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Refresh Token 확인 및 Rotation
        var rtCookie = CookieUtil.getCookie(request, "refresh_token").orElse(null);
        if (rtCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 토큰 재발급 (Access Token 문자열 받기)
            String newAt = tokenRotationService.rotateTokens(rtCookie.getValue(), response)
                .accessToken();

            // =================================================================
            // [수정 완료] DB 조회 없이 토큰에서 정보 추출하여 인증 처리
            // =================================================================

            // 1. 새 토큰 디코딩
            DecodedJWT decoded = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
                .verify(newAt);

            // 2. Claim에서 ID와 Role 추출
            String userId = decoded.getClaim("userId").asString();
            String role = decoded.getClaim("role").asString();

            // role null 처리 (기본값)
            if (role == null || role.isBlank()) {
                role = "ROLE_USER";
            }

            // 3. UserDetails 생성 (DB 조회 X)
            var authorities = List.of(new SimpleGrantedAuthority(role));

            UserDetails principal = User.builder()
                .username(userId)
                .password("") // 비밀번호 불필요
                .authorities(authorities)
                .build();

            // 4. Authentication 객체 생성 및 Context 저장
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);

            authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. 다음 필터 진행
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            // 재발급 실패 등
            filterChain.doFilter(request, response);
        }
    }
}