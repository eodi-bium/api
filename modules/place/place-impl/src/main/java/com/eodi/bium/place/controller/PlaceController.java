package com.eodi.bium.place.controller;

import com.eodi.bium.place.dto.PlaceRequest;
import com.eodi.bium.place.dto.PlaceResponse;
import com.eodi.bium.place.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/place")
public class PlaceController {

    private final PlaceService placeService;

    @PostMapping
    public ResponseEntity<PlaceResponse> postPlaces(
        @Valid @RequestBody PlaceRequest placeRequest) {
        System.out.println(placeRequest);
        return ResponseEntity.ok(placeService.getPlaces(placeRequest));
    }
}
