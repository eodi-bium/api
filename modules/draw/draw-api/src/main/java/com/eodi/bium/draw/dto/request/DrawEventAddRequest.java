package com.eodi.bium.draw.dto.request;

import java.time.LocalDateTime;

public record DrawEventAddRequest(
    String giftName,
    String giftPictureUrl,
    LocalDateTime startDate,
    LocalDateTime endDate,
    LocalDateTime announcementDate
) {

}
