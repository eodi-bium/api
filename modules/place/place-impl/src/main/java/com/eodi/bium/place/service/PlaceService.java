package com.eodi.bium.place.service;

import com.eodi.bium.place.dto.PlaceRequest;
import com.eodi.bium.place.dto.PlaceResponse;
import com.eodi.bium.place.entity.Place;
import com.eodi.bium.place.mapper.PlaceResponseMapper;
import com.eodi.bium.place.repository.PlaceRepository;
import com.eodi.bium.review.error.CustomException;
import com.eodi.bium.review.error.ExceptionMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlaceService {

    private PlaceRepository placeRepository;

    public PlaceResponse getPlaces(PlaceRequest placeRequest) {
        List<Place> places = placeRepository.findPlaceWithRadius(placeRequest.latitude(),
            placeRequest.longitude(), placeRequest.km() * 1000);
        if (places.isEmpty()) {
            throw new CustomException(ExceptionMessage.PLACES_NOT_FOUND);
        }
        return toPlaceResponse(places);
    }

    private PlaceResponse toPlaceResponse(List<Place> place) {
        return PlaceResponseMapper.fromEntity(place);
    }
}
