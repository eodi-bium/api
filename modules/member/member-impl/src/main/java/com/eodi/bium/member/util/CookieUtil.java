package com.eodi.bium.member.util;

import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import com.eodi.bium.member.properties.JwtProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final JwtProperties jwtProperties;

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
            .filter(c -> name.equals(c.getName()))
            .findFirst();
    }

    public ResponseCookie deleteRefreshCookie() {
        return ResponseCookie.from("refresh_token", "")
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(0)
            .domain(parseDomain(jwtProperties.getFrontEndDeployUrl()))
            .build();
    }

    public ResponseCookie buildCookies(String value) {
        return ResponseCookie.from("refresh_token", value)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(jwtProperties.getRefreshExpirationTime())
            .domain(parseDomain(jwtProperties.getFrontEndDeployUrl()))
            .build();
    }

    private String parseDomain(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain;
        } catch (URISyntaxException e) {
            throw new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
    }
}