package com.eodi.bium.review.member.controller;

import com.eodi.bium.review.member.dto.request.LoginRequest;
import com.eodi.bium.review.member.dto.response.LoginResponse;
import com.eodi.bium.review.member.service.LogoutService;
import com.eodi.bium.review.member.service.MyPageService;
import com.eodi.bium.review.member.service.TokenRotationService;
import com.eodi.bium.review.member.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MyPageService myPageService;
    private final TokenRotationService tokenRotationService;
    private final LogoutService logoutService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> normalLogin(@RequestBody LoginRequest loginRequest,
        HttpServletResponse response) {
        LoginResponse loginResponse = myPageService.getNickname(loginRequest);
        tokenRotationService.issueTokens(loginRequest.getId(), response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        var rtCookie = CookieUtil.getCookie(request, "refresh_token").orElse(null);
        tokenRotationService.rotateTokens(rtCookie.getValue(), response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
