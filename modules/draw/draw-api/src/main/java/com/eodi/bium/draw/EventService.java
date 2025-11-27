package com.eodi.bium.draw;

import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import java.util.List;

public interface EventService {

    List<Long> listAvailableEvents();

    void addEvent(DrawEventAddRequest request);
}

