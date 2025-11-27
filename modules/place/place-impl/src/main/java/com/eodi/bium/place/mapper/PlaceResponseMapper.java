package com.eodi.bium.place.mapper;

import com.eodi.bium.place.dto.response.PlaceResponse;
import com.eodi.bium.place.dto.response.SinglePlaceResponse;
import com.eodi.bium.place.entity.Place;
import java.util.List;

public class PlaceResponseMapper {

    public static PlaceResponse fromEntity(List<Place> places) {
        List<SinglePlaceResponse> placeResponses = places.stream()
            .map(place -> new SinglePlaceResponse(
                place.getName(),
                place.getLongitude(),
                place.getLatitude(),
                place.getPhoneNumber()
            )).toList();
        return new PlaceResponse(placeResponses);
    }
}
