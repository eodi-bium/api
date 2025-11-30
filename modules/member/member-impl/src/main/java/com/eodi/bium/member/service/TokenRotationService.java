package com.eodi.bium.member.service;

import com.eodi.bium.member.component.AccessTokenHandler;
import com.eodi.bium.member.dto.response.AtResponse;
import com.eodi.bium.member.util.CookieUtil;
import com.eodi.bium.member.util.RefreshTokenUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenRotationService {

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
