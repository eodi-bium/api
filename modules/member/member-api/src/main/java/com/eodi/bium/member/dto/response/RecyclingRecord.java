package com.eodi.bium.member.dto.response;

import com.eodi.bium.global.enums.RecyclingType;

public record RecyclingRecord(RecyclingType recyclingType, Long count, Long point) {

}
