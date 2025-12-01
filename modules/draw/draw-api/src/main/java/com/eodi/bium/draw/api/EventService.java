package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.request.EventJoinRequest;
import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.dto.response.MyPointResponse;
import com.eodi.bium.draw.dto.response.UserEventStatusResponse;
import java.util.List;

public interface EventService {

    UserEventStatusResponse getMyEventStatus(String userId, Long eventId);

    MyPointResponse getMyPoint(String userId);

    void joinEvent(String memberId, EventJoinRequest request);

    EventResponse getEvent();

    void addEvent(DrawEventAddRequest request);

    List<EventRecord> getEventWithMemberId(String memberId);
}

