package com.eodi.bium.draw.controller;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.EventJoinRequest;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.dto.response.MyPointResponse;
import com.eodi.bium.global.annotation.AuthUserId;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @PostMapping("/join")
    public void joinEvent(
        @AuthUserId
        String userId,
        @RequestBody
        EventJoinRequest request
    ) {
        eventService.joinEvent(userId, request);
    }

    @GetMapping("/point")
    public MyPointResponse getMyPoint(
        @AuthUserId
        String userId
    ) {
        return eventService.getMyPoint(userId);
    }

    @GetMapping("/lastest")
    public EventResponse getEvent(
        @AuthUserId
        String userId
    ) {
        return eventService.getEvent(userId);
    }
}
