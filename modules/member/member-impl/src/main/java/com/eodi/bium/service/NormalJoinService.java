package com.eodi.bium.service;

import com.eodi.bium.dto.request.JoinRequest;
import com.eodi.bium.entity.Member;
import com.eodi.bium.error.CustomException;
import com.eodi.bium.error.ExceptionMessage;
import com.eodi.bium.repository.MemberRepository;
import com.eodi.bium.util.NicknameGenerateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NormalJoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(JoinRequest normalJoinRequest) {
        String userId = normalJoinRequest.getId();

        if (memberRepository.existsByMemberId(userId)) {
            throw new CustomException(ExceptionMessage.DUPLICATE_USERNAME);
        }

        String nickname = NicknameGenerateUtil.generate();
        String password = bCryptPasswordEncoder.encode(normalJoinRequest.getPassword());
        Member member = Member.builder()
            .memberId(userId)
            .password(password)
            .role("ROLE_USER")
            .provider(null)
            .nickname(nickname)
            .build();
        memberRepository.save(member);
        System.out.println("기본 회원가입 완료, " + nickname);
    }
}
