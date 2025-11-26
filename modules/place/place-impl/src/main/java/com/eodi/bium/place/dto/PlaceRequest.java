package com.eodi.bium.place.dto;

import com.eodi.bium.place.enums.RecyclingType;

public record PlaceRequest(RecyclingType recyclingType,double latitude,double longitude,double km) {

}