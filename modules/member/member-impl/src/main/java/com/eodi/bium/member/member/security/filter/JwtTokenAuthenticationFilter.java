package com.eodi.bium.member.member.security.filter;


import static com.auth0.jwt.JWT.require;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.eodi.bium.member.member.entity.Member;
import com.eodi.bium.member.member.properties.JwtProperties;
import com.eodi.bium.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JwtTokenAuthenticationFilter extends BasicAuthenticationFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private final MemberRepository memberRepository;

    public JwtTokenAuthenticationFilter(AuthenticationManager authManager,
        MemberRepository memberRepository) {
        super(authManager);
        this.memberRepository = memberRepository;
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
            var decoded = require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
                .verify(accessToken);

            String userId = decoded.getClaim("userId").asString();
            if (userId == null || userId.isBlank()) {
                chain.doFilter(request, response);
                return;
            }

            Authentication existing = SecurityContextHolder.getContext().getAuthentication();
            if (existing != null && existing.isAuthenticated()) {
                chain.doFilter(request, response);
                return;
            }

            Member member = memberRepository.findById(userId).orElse(null);
            if (member == null) {
                chain.doFilter(request, response);
                return;
            }
            String role = Objects.toString(member.getRole(), "ROLE_USER");
            var authorities = List.of(new SimpleGrantedAuthority(role));

            UserDetails principal = User.builder()
                .username(member.getMemberId())
                .password(member.getPassword() != null ? member.getPassword() : "")
                .authorities(authorities)
                .build();

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, null,
                    authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (TokenExpiredException ex) {
            chain.doFilter(request, response);
        }
    }
}
