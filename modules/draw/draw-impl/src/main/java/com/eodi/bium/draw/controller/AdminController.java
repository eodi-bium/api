package com.eodi.bium.draw.controller;

import com.eodi.bium.draw.api.DrawService;
import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.request.DrawPointRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final DrawService drawService;

    @PostMapping("/event/add")
    public void addDrawEvent(DrawEventAddRequest request) {
        eventService.addEvent(request);
    }

    @PostMapping("/point/add")
    public void joinDraw(
        @RequestBody DrawPointRequest drawPointRequest) {
        drawService.joinDraw(drawPointRequest);
    }
//
//    @PostMapping("/draw/start")
//    public DrawResultResponse startDraw(@RequestBody DrawStartRequest drawStartRequest) {
//        return drawService.startDraw(drawStartRequest);
//    }
}
