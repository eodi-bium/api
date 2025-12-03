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

        // 1. [TrashRecord] 통계 로직 (기존 유지)
        // List로 받아 메모리에서 합계를 계산합니다.
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

        // 2. [EventRecord] Slice 변환 로직 (변경됨)
        // stream()을 쓰지 않고 Slice 인터페이스의 map()을 바로 사용합니다.
        // 이렇게 해야 페이징 메타데이터(hasNext 등)가 유지됩니다.
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
