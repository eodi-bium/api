package com.eodi.bium.draw.dto.response;

import java.time.LocalDateTime;

// 1. 전체 응답을 감싸는 Root Wrapper (JSON의 최상위 "data" 키 대응)
public record EventResponse(
    String giftName,
    Long count,
    String giftImageUrl,
    EventPeriod period,
    EventStats stats
) {

    // 기간 정보
    public record EventPeriod(
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocalDateTime announcementDate
    ) {

    }

    // 통계 정보
    public record EventStats(
        long totalAccumulatedPoints,
        long totalParticipants
    ) {

    }
}