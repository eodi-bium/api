package com.eodi.bium.review.member.component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.eodi.bium.review.error.CustomException;
import com.eodi.bium.review.error.ExceptionMessage;
import com.eodi.bium.review.member.properties.JwtProperties;
import com.eodi.bium.review.member.repository.MemberRepository;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessTokenHandler {

    private final MemberRepository memberRepository;

    public String validateAndGenerate(String memberId) {
        return memberRepository.findById(memberId)
            .map(member -> JWT.create()
                .withSubject(member.getMemberId())
                .withClaim("userId", member.getMemberId())
                .withClaim("nickname", member.getNickname())
                .withExpiresAt(
                    new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET)))
            .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND));
    }
}
