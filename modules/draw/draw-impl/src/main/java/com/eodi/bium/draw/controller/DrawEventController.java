package com.eodi.bium.draw.controller;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/event")
public class DrawEventController {

    private final EventService eventService;

    @PostMapping("/add")
    public void addDrawEvent(DrawEventAddRequest request) {
        eventService.addEvent(request);
    }

    @GetMapping("/latest")
    public List<Long> listDrawEvents() {
        return eventService.getLastestEvent();
    }
}
