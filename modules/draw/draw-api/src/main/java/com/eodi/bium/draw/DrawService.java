package com.eodi.bium.draw;

import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import java.util.List;

public interface DrawService {

    void joinDraw(DrawPointRequest request);

    void startDraw(DrawStartRequest request);

    List<String> getDrawJoinedMembers(Long eventId);
}

