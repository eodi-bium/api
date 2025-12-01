package com.eodi.bium.member.security;

import com.eodi.bium.global.error.ExceptionMessage;
import com.eodi.bium.member.component.AccessTokenHandler;
import com.eodi.bium.member.dto.response.AtResponse;
import com.eodi.bium.member.entity.Member;
import com.eodi.bium.member.repository.MemberRepository;
import com.eodi.bium.member.security.filter.JwtTokenAuthenticationFilter;
import com.eodi.bium.member.security.filter.RefreshRotationFilter;
import com.eodi.bium.member.security.principal.PrincipalDetails;
import com.eodi.bium.member.security.provider.GoogleUserInfo;
import com.eodi.bium.member.security.provider.KakaoUserInfo;
import com.eodi.bium.member.security.provider.OAuth2UserInfo;
import com.eodi.bium.member.util.CookieUtil;
import com.eodi.bium.member.util.NicknameGenerateUtil;
import com.eodi.bium.member.util.RefreshTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final PrincipalOAuth2UserService principalOauth2UserService;
    private final TokenRotationService tokenRotationService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        AuthenticationManager authenticationManager) throws Exception {
        http
            .securityMatcher("/admin/**", "/memberInfo")
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            // 예외 처리 (공통)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    createUnauthorizedResponse(response);
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    createForbiddenResponse(response);
                })
            )

            // 필터 등록 (모든 요청에 대해 동작하지만, 헤더/쿠키가 없으면 내부 로직에서 통과됨)
            // 최적화된 생성자 적용 (Repository 제거됨)
            .addFilterBefore(
                new JwtTokenAuthenticationFilter(authenticationManager),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(
                new RefreshRotationFilter(tokenRotationService),
                JwtTokenAuthenticationFilter.class)

            // URL 권한 설정 (순서 중요: 구체적인 것 -> 일반적인 것)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/memberInfo").hasRole("USER")
                .anyRequest().denyAll() // 이 필터 체인에 해당하지만 위에서 명시되지 않은 다른 모든 요청은 거부
            );
        return http.build();
    }

    // JWT 필터 통하지 않는 것
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    createUnauthorizedResponse(response);
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    createForbiddenResponse(response);
                })
                .requestMatchers("/test").authenticated()
                .anyRequest().permitAll()
            )

            // OAuth2 설정
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(user -> user.userService(principalOauth2UserService))
                .successHandler(oAuth2LoginSuccessHandler)
            );

        return http.build();
    }

    // --- 이하 기존 유틸 메서드 및 내부 클래스 동일 ---

    public void createUnauthorizedResponse(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"message\": \"" + ExceptionMessage.UNAUTHORIZED.getMessage() + "\"}"
            );
        } catch (IOException e) {
        }
    }

    public void createForbiddenResponse(HttpServletResponse response) {
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                "{\"message\": \"" + ExceptionMessage.NOT_ENOUGH_ACCESS.getMessage() + "\"}"
            );
        } catch (IOException e) {
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

        private final TokenRotationService tokenRotationService;
        @Value("${bium.frontend-deploy-url}")
        private String frontendDeployUrl;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            Member member = principal.getMember();
            tokenRotationService.issueRefreshToken(member.getMemberId(), response);
            response.sendRedirect(frontendDeployUrl + "/redirect/login");
        }
    }

    @Component
    @AllArgsConstructor
    public static class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

        private final MemberRepository memberRepository;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;

        @Override
        public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
            OAuth2User oAuth2User = super.loadUser(userRequest);
            OAuth2UserInfo oAuth2UserInfo = null;
            if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
            } else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
                oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
            }
            String userId = oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId();
            String provider = oAuth2UserInfo.getProvider();
            String nickname = NicknameGenerateUtil.generate();
            String password = bCryptPasswordEncoder.encode("temporaryPassword");
            String role = "ROLE_USER";
            Member member = memberRepository.findById(userId).orElse(null);
            if (member == null) {
                member = Member.builder()
                    .memberId(userId)
                    .password(password)
                    .role(role)
                    .nickname(nickname)
                    .provider(provider)
                    .build();
                memberRepository.save(member);
            }
            return new PrincipalDetails(member, oAuth2User.getAttributes());
        }
    }

    @Component
    @RequiredArgsConstructor
    public static class TokenRotationService {

        private final RefreshTokenUtil refreshTokenUtil;
        private final AccessTokenHandler accessTokenHandler;

        public void issueRefreshToken(String userId, HttpServletResponse response) {
            String newRtPlain = refreshTokenUtil.issue(userId);
            setRefreshToken(response, newRtPlain);
        }

        public void issueTokens(String userId, HttpServletResponse response) {
            String newRtPlain = refreshTokenUtil.issue(userId);
            String newAt = accessTokenHandler.validateAndGenerate(userId);

            setAccessToken(response, newAt);
            setRefreshToken(response, newRtPlain);
        }

        public AtResponse rotateTokens(String refreshToken, HttpServletResponse response) {
            var newRtCookie = refreshTokenUtil.validateAndRotate(refreshToken);
            String newAt = accessTokenHandler.validateAndGenerate(newRtCookie.userId());

            setAccessToken(response, newAt);
            setRefreshToken(response, newRtCookie.newRtPlain());
            return new AtResponse(newAt);
        }

        private void setAccessToken(HttpServletResponse response, String newAt) {
            response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAt);
        }

        private void setRefreshToken(HttpServletResponse response, String newRtPlain) {
            response.setHeader(HttpHeaders.SET_COOKIE,
                CookieUtil.buildCookies(newRtPlain).toString());
        }
    }
}