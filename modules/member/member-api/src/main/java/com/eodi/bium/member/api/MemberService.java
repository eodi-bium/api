package com.eodi.bium.member.api;

import com.eodi.bium.member.dto.response.LoginResponse;

public interface MemberService {

    LoginResponse getNickname(String memberId);

    void findMember(String memberId);
}
