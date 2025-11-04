package com.eodi.bium.service;

import com.eodi.bium.dto.request.LoginRequest;
import com.eodi.bium.dto.response.LoginResponse;
import com.eodi.bium.entity.Member;
import com.eodi.bium.error.CustomException;
import com.eodi.bium.error.ExceptionMessage;
import com.eodi.bium.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NormalLoginService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginResponse getNickname(LoginRequest loginRequest) {
        String memberId = loginRequest.getMemberId();
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
            throw new CustomException(ExceptionMessage.INVALID_PASSWORD);
        }
        System.out.println("로그인 완료: " + member.getMemberId() + " 닉네임: " + member.getNickname());
        return LoginResponse.builder()
            .nickname(member.getNickname()).build();
    }
}
