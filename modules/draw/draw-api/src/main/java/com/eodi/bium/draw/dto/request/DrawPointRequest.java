package com.eodi.bium.draw.dto.request;

import com.eodi.bium.global.enums.RecyclingType;
import java.util.List;

public record DrawPointRequest(
    List<TypeAndCount> typeAndCounts,
    String memberId
) {

    public record TypeAndCount(
        RecyclingType recyclingType,
        Long count
    ) {

    }
}