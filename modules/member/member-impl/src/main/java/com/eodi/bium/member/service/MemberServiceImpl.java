package com.eodi.bium.member.service;

import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import com.eodi.bium.member.api.MemberService;
import com.eodi.bium.member.dto.request.LoginRequest;
import com.eodi.bium.member.dto.response.LoginResponse;
import com.eodi.bium.member.member.entity.Member;
import com.eodi.bium.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void findMember(String memberId) {
        memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND));
    }

    public LoginResponse login(LoginRequest loginRequest) {
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

    @Override
    public LoginResponse getNickname(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND));

        return LoginResponse.builder()
            .nickname(member.getNickname()).build();
    }
}
