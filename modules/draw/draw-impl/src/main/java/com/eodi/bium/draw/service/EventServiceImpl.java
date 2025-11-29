package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.entity.DrawEvent;
import com.eodi.bium.draw.repsoitory.DrawEventRepository;
import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    private final DrawEventRepository drawEventRepository;

    @Override
    public EventResponse getEvent(String userId) {
        DrawEvent availableEvent = drawEventRepository.findAvailableEvent();
        if (availableEvent == null) {
            throw new CustomException(ExceptionMessage.EVENT_NOT_FOUND);
        }
        return new EventResponse(
            availableEvent.getGiftName(),
            availableEvent.getGiftPicture(),
            new EventResponse.EventPeriod(
                availableEvent.getStartDate(),
                availableEvent.getEndDate(),
                availableEvent.getAnnouncementDate()
            ),
            new EventResponse.EventStats(
                0L,
                0L
            ),
            new EventResponse.UserEventStatus(
                0L,
                0.0
            )
        );
    }

    @Override
    public void addEvent(DrawEventAddRequest request) {

        if (drawEventRepository.findAvailableEvent() != null) {
            throw new CustomException(ExceptionMessage.EVENT_ALREADY_ONGOING);
        }

        DrawEvent drawEvent = DrawEvent.builder()
            .giftName(request.giftName())
            .giftPicture(request.giftPictureUrl())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .announcementDate(request.announcementDate())
            .build();
        drawEventRepository.save(drawEvent);
    }
}

