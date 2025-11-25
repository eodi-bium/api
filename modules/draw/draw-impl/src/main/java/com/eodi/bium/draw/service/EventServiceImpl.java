package com.eodi.bium.draw.service;

import com.eodi.bium.draw.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    @Override
    public void getEventInfo(Long eventId) {

    }
}

