package com.eodi.bium.draw.controller;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.response.EventResponse;
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
public class DrawEventController {

    private final EventService eventService;

    @PostMapping("/add")
    public void addDrawEvent(@RequestBody DrawEventAddRequest request) {
        eventService.addEvent(request);
    }

    @GetMapping("/active")
    public EventResponse getEvent(
        @AuthUserId
        String userId
    ) {
        return eventService.getEvent(userId);
    }
}
