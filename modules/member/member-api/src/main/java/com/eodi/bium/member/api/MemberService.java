package com.eodi.bium.member.api;

import com.eodi.bium.member.dto.request.LoginRequest;
import com.eodi.bium.member.dto.response.LoginResponse;

public interface MemberService {

    LoginResponse login(LoginRequest loginRequest);

    LoginResponse getNickname(String memberId);
}
