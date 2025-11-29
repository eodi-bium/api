package com.eodi.bium.place.dto.request;


import com.eodi.bium.global.enums.RecyclingType;

public record PlaceRequest(RecyclingType recyclingType, double latitude, double longitude,
                           double km) {

}