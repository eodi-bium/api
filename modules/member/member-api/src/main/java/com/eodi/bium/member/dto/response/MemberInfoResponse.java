package com.eodi.bium.member.dto.response;

import com.eodi.bium.global.enums.RecyclingType;
import java.time.LocalDateTime;
import java.util.List;

public record MemberInfoResponse(String nickname, List<RecyclingRecord> records,
                                 List<SingleEventRecord> eventRecords) {

    public record SingleEventRecord(String name, Long giftCount, LocalDateTime startDate,
                                    LocalDateTime endDate, LocalDateTime announceDate,
                                    Long myPoint) {

    }

    public record RecyclingRecord(RecyclingType recyclingType, Long count, Long point) {

    }
}
