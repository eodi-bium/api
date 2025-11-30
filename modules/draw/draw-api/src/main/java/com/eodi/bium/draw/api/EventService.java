package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import java.util.List;

public interface EventService {

    List<Long> getLastestEvent();

    void addEvent(DrawEventAddRequest request);
}

