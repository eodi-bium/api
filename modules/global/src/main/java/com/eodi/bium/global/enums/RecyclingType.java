package com.eodi.bium.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RecyclingType {
    BATTERY(20),
    LIGHT(100),
    PHONE(200),
    CLOTHES(200);

    private final int point;
}