package com.eodi.bium.draw.controller;

import com.eodi.bium.draw.DrawService;
import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/draw")
public class DrawController {

    DrawService drawService;

    @PostMapping("/join")
    public void joinDraw(DrawPointRequest drawPointRequest) {
        drawService.joinDraw(drawPointRequest);
    }

    @PostMapping("/start")
    public void startDraw(DrawStartRequest drawStartRequest) {
        drawService.startDraw(drawStartRequest);
    }
}
