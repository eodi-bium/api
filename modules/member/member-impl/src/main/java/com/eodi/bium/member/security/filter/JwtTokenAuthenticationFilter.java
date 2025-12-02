package com.eodi.bium.member.security.filter;

import static com.auth0.jwt.JWT.require;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eodi.bium.member.properties.JwtProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    public JwtTokenAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = authorizationHeader.substring(BEARER_PREFIX.length());

        try {
            // 1. 토큰 검증
            DecodedJWT decoded = require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
                .verify(accessToken);

            // 2. 이미 인증된 상태라면 패스 (RefreshRotationFilter 통과 시 등)
            Authentication existing = SecurityContextHolder.getContext().getAuthentication();
            if (existing != null && existing.isAuthenticated()) {
                chain.doFilter(request, response);
                return;
            }

            // 3. 토큰에서 데이터 추출
            String userId = decoded.getClaim("userId").asString();
            // [핵심] DB 조회 대신 토큰 Claim에서 Role 추출
            String role = decoded.getClaim("role").asString();

            // userId가 없으면 실패 처리
            if (userId == null || userId.isBlank()) {
                chain.doFilter(request, response);
                return;
            }

            // role이 비어있을 경우 기본값 설정 (선택 사항)
            if (role == null || role.isBlank()) {
                role = "ROLE_USER";
            }

            // 4. UserDetails 생성 (DB 접근 없음)
            var authorities = List.of(new SimpleGrantedAuthority(role));

            UserDetails principal = User.builder()
                .username(userId)
                .password("") // 비밀번호 불필요
                .authorities(authorities)
                .build();

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (TokenExpiredException ex) {
            // 토큰 만료 -> 통과 (인증 실패 상태로 뒤 필터로 이동)
            chain.doFilter(request, response);
        } catch (Exception ex) {
            // 그 외 검증 오류 -> 통과
            chain.doFilter(request, response);
        }
    }
}