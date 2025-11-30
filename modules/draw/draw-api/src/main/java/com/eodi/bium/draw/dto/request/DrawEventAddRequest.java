package com.eodi.bium.draw.dto.request;

import java.time.LocalDateTime;

public record DrawEventAddRequest(
    String giftName,
    String giftImageUrl,
    Long count,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime announcementDate
) {

}
