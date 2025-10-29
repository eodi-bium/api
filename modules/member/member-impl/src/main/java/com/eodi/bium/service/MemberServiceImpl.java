package com.eodi.bium.service;

import com.eodi.bium.MemberService;
import com.eodi.bium.dto.dto.Member.MemberResponse;
import com.eodi.bium.entity.Member;
import com.eodi.bium.repository.MemberRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void joinMember(UUID userId) {
        Member member = Member.create(userId);
        memberRepository.save(member);
    }

    @Override
    public Optional<MemberResponse> findMember(UUID userId) {
        return memberRepository.findById(userId).map(m -> new MemberResponse(m.getId()));
    }
}
