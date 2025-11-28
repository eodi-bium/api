package com.eodi.bium.draw.controller;

import com.eodi.bium.draw.api.DrawService;
import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import com.eodi.bium.draw.dto.response.DrawResultResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/draw-point")
public class DrawPointController {

    DrawService drawService;

    @PostMapping("/join")
    public void joinDraw(
        @RequestBody DrawPointRequest drawPointRequest) {
        drawService.joinDraw(drawPointRequest);
    }

    @PostMapping("/start")
    public DrawResultResponse startDraw(@RequestBody DrawStartRequest drawStartRequest) {
        return drawService.startDraw(drawStartRequest);
    }
}
