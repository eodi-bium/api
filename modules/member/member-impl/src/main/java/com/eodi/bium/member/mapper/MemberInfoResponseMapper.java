package com.eodi.bium.member.mapper;

import com.eodi.bium.draw.dto.response.TrashRecordResponse;
import com.eodi.bium.global.enums.RecyclingType;
import com.eodi.bium.member.dto.response.MemberInfoResponse;
import com.eodi.bium.member.dto.response.RecyclingRecord;
import com.eodi.bium.member.entity.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MemberInfoResponseMapper {

    public static MemberInfoResponse fromDrawResponse(Member member,
        List<TrashRecordResponse> records) {
        // type별 count
        Map<RecyclingType, Long> recyclingCounts = records.stream()
            .collect(Collectors.groupingBy(
                TrashRecordResponse::recyclingType,
                Collectors.summingLong(TrashRecordResponse::count)
            ));

        // dto로 변환
        List<RecyclingRecord> recyclingRecords = recyclingCounts.entrySet().stream()
            .map(single -> new RecyclingRecord(single.getKey(), single.getValue(),
                single.getKey().getPoint() * single.getValue()))
            .toList();

        return new MemberInfoResponse(
            member.getNickname(), recyclingRecords);
    }
}
