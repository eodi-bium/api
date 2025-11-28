package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import com.eodi.bium.draw.dto.response.DrawResultResponse;
import java.util.List;

public interface DrawService {

    void joinDraw(DrawPointRequest request);

    DrawResultResponse startDraw(DrawStartRequest request);

    List<String> getDrawJoinedMembers(Long eventId);
}

