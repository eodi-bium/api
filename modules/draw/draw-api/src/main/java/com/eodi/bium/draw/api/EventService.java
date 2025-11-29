package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.response.EventResponse;

public interface EventService {

    EventResponse getEvent(String userId);

    void addEvent(DrawEventAddRequest request);
}

