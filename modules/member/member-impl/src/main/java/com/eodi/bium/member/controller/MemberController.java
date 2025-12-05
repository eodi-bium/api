package com.eodi.bium.member.controller;

import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import com.eodi.bium.member.dto.response.AtResponse;
import com.eodi.bium.member.security.SecurityConfig.TokenRotationService;
import com.eodi.bium.member.service.LogoutService;
import com.eodi.bium.member.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final CookieUtil cookieUtil;
    private final TokenRotationService tokenRotationService;
    private final LogoutService logoutService;


    @PostMapping("/refresh")
    public ResponseEntity<AtResponse> refresh(HttpServletRequest request,
        HttpServletResponse response) {
        String scheme = request.getScheme();
        boolean isSecure = request.isSecure();
        String remoteAddr = request.getRemoteAddr();

        System.out.println("=== Spring Identity Check ===");
        System.out.println("Scheme: " + scheme);       // http 또는 https
        System.out.println("isSecure: " + isSecure);   // false 또는 true
        System.out.println("Real IP?: " + remoteAddr); // 127.0.0.1 또는 실제 IP
        System.out.println("=============================");
        Cookie rtCookie = cookieUtil.getCookie(request, "refresh_token").orElse(null);
        if (rtCookie == null) {
            throw new CustomException(ExceptionMessage.NOT_FOUND_RT);
        }
        return ResponseEntity.ok(tokenRotationService.rotateTokens(rtCookie.getValue(), response));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
