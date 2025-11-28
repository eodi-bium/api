package com.eodi.bium.member.member.controller;

import com.eodi.bium.member.dto.request.LoginRequest;
import com.eodi.bium.member.dto.response.AtResponse;
import com.eodi.bium.member.dto.response.LoginResponse;
import com.eodi.bium.member.member.util.CookieUtil;
import com.eodi.bium.member.service.LogoutService;
import com.eodi.bium.member.service.MemberServiceImpl;
import com.eodi.bium.member.service.TokenRotationService;
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

    private final MemberServiceImpl memberServiceImpl;
    private final TokenRotationService tokenRotationService;
    private final LogoutService logoutService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> normalLogin(@RequestBody LoginRequest loginRequest,
        HttpServletResponse response) {
        LoginResponse loginResponse = memberServiceImpl.login(loginRequest);
        tokenRotationService.issueTokens(loginRequest.getId(), response);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AtResponse> refresh(HttpServletRequest request,
        HttpServletResponse response) {
        var rtCookie = CookieUtil.getCookie(request, "refresh_token").orElse(null);
        return ResponseEntity.ok(tokenRotationService.rotateTokens(rtCookie.getValue(), response));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        logoutService.logout(request, response);
        return ResponseEntity.ok().build();
    }
}
