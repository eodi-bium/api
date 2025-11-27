package com.eodi.bium.draw.dto.request;

public record DrawPointRequest(
    Long recordId,
    Long eventId,
    String memberId,
    Long point
) {

}

