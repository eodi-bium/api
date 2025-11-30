package com.eodi.bium.member.service;

import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import com.eodi.bium.member.api.MemberService;
import com.eodi.bium.member.dto.response.LoginResponse;
import com.eodi.bium.member.entity.Member;
import com.eodi.bium.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public void findMember(String memberId) {
        memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND));
    }

    @Override
    public LoginResponse getNickname(String memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ExceptionMessage.USER_NOT_FOUND));

        return LoginResponse.builder()
            .nickname(member.getNickname()).build();
    }
}
