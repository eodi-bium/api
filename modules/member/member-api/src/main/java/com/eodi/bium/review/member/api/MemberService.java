package com.eodi.bium.review.member.api;

import com.eodi.bium.review.member.dto.request.LoginRequest;
import com.eodi.bium.review.member.dto.response.LoginResponse;

public interface MemberService {

    LoginResponse getNickname(LoginRequest loginRequest);
}
