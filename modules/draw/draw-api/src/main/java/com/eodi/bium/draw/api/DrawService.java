package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.request.DrawStartRequest;
import com.eodi.bium.draw.dto.response.DrawResultResponse;

public interface DrawService {

    DrawResultResponse startDraw(DrawStartRequest request);

}
