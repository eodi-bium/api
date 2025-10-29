package com.eodi.bium;

import com.eodi.bium.dto.dto.Member.MemberResponse;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {

    Optional<MemberResponse> findMember(UUID userId);

    void joinMember(UUID userId);
}
