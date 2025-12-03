package com.eodi.bium.member.mapper;

import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.draw.dto.response.TrashRecordResponse;
import com.eodi.bium.global.enums.RecyclingType;
import com.eodi.bium.member.dto.response.MemberInfoResponse;
import com.eodi.bium.member.dto.response.MemberInfoResponse.RecyclingRecord;
import com.eodi.bium.member.dto.response.MemberInfoResponse.SingleEventRecord;
import com.eodi.bium.member.entity.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Slice;

public class MemberInfoResponseMapper {

    public static MemberInfoResponse fromDrawResponse(Member member,
        List<TrashRecordResponse> records, Slice<EventRecord> eventRecords) {

        Map<RecyclingType, Long> recyclingCounts = records.stream()
            .collect(Collectors.groupingBy(
                TrashRecordResponse::recyclingType,
                Collectors.summingLong(TrashRecordResponse::count)
            ));

        List<RecyclingRecord> recyclingRecords = recyclingCounts.entrySet().stream()
            .map(single -> new RecyclingRecord(
                single.getKey(),
                single.getValue(),
                single.getKey().getPoint() * single.getValue()
            ))
            .toList();

        Slice<SingleEventRecord> eventRecordDtos = eventRecords.map(single ->
            new SingleEventRecord(
                single.name(),
                single.giftCount(),
                single.startDate(),
                single.endDate(),
                single.announceDate(),
                single.myPoint()
            )
        );

        return new MemberInfoResponse(
            member.getNickname(),
            recyclingRecords,
            eventRecordDtos
        );
    }
}
