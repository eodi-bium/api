package com.eodi.bium.member.dto.response;

import java.util.List;

public record MemberInfoResponse(String nickname, List<RecyclingRecord> records) {

}
