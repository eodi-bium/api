package com.eodi.bium.review.member.service;

import com.eodi.bium.review.error.CustomException;
import com.eodi.bium.review.error.ExceptionMessage;
import com.eodi.bium.review.member.api.MemberService;
import com.eodi.bium.review.member.dto.request.LoginRequest;
import com.eodi.bium.review.member.dto.response.LoginResponse;
import com.eodi.bium.review.member.entity.Member;
import com.eodi.bium.review.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginResponse getNickname(LoginRequest loginRequest) {
        String memberId = loginRequest.getId();
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
