package com.eodi.bium.draw.dto.response;

import com.eodi.bium.global.enums.RecyclingType;

public record TrashRecordResponse(RecyclingType recyclingType, Long count) {

}
