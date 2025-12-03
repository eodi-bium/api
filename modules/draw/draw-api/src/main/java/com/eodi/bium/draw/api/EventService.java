package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.request.EventJoinRequest;
import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.dto.response.MyPointResponse;
import com.eodi.bium.draw.dto.response.UserEventStatusResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface EventService {

    UserEventStatusResponse getMyEventStatus(String userId, Long eventId);

    MyPointResponse getMyPoint(String userId);

    void joinEvent(String memberId, EventJoinRequest request);

    EventResponse getEvents(Pageable pageable);

    void addEvent(DrawEventAddRequest request);

    Slice<EventRecord> getEventWithMemberId(String memberId, Pageable pageable);
}

