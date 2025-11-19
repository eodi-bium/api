package com.eodi.bium.global.member.service;

import com.eodi.bium.global.member.util.CookieUtil;
import com.eodi.bium.global.member.util.RefreshTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final RefreshTokenUtil refreshTokenUtil;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        var rtCookie = CookieUtil.getCookie(request, "refresh_token").orElse(null);
        if (rtCookie != null) {
            refreshTokenUtil.revoke(rtCookie.getValue());
        }
        response.addHeader(HttpHeaders.SET_COOKIE, CookieUtil.deleteRefreshCookie().toString());
    }
}
