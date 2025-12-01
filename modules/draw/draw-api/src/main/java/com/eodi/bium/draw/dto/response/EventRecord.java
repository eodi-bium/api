package com.eodi.bium.draw.dto.response;

import java.time.LocalDateTime;

public record EventRecord(String name, Long giftCount, LocalDateTime startDate,
                          LocalDateTime endDate, LocalDateTime announceDate,
                          Long myPoint) {

}
