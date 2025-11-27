package com.eodi.bium.place.service;

import com.eodi.bium.place.dto.request.PlaceRequest;
import com.eodi.bium.place.dto.response.PlaceResponse;

public interface PlaceService {

    PlaceResponse getPlaces(PlaceRequest placeRequest);
    
}
