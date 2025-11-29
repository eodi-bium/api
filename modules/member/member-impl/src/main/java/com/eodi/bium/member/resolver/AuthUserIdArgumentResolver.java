package com.eodi.bium.member.resolver;

import com.eodi.bium.global.annotation.AuthUserId;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUserId.class)
            && parameter.getParameterType().equals(String.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 1. authentication 자체가 null인지 먼저 체크해야 NPE를 방지합니다.
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        // 2. principal이 null이어도 instanceof는 안전하게 false를 반환합니다.
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return null;
    }
}

