package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.request.EventJoinRequest;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.dto.response.MyPointResponse;

public interface EventService {

    MyPointResponse getMyPoint(String userId);

    void joinEvent(String memberId, EventJoinRequest request);

    EventResponse getEvent(String userId);

    void addEvent(DrawEventAddRequest request);
}

