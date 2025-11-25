package com.eodi.bium.draw.service;

import com.eodi.bium.draw.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.entity.DrawEvent;
import com.eodi.bium.draw.repsoitory.DrawEventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    private final DrawEventRepository drawEventRepository;

    @Override
    public void addEvent(DrawEventAddRequest request) {
        DrawEvent drawEvent = DrawEvent.builder()
            .giftName(request.giftName())
            .giftPicture(request.giftPictureUrl()).build();
        drawEventRepository.save(drawEvent);
    }

    @Override
    public List<Long> listAvailableEvents() {
        return drawEventRepository.findAllAvailableEventIds();
    }
}

