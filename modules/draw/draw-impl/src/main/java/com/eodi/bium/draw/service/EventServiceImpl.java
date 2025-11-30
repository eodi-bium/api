package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.entity.Event;
import com.eodi.bium.draw.repsoitory.EventRepository;
import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {


    private final EventRepository eventRepository;

    @Override
    public EventResponse getEvent(String userId) {
        Event availableEvent = eventRepository.findAvailableEvent();
        if (availableEvent == null) {
            throw new CustomException(ExceptionMessage.EVENT_NOT_FOUND);
        }
        return new EventResponse(
            availableEvent.getGiftName(),
            availableEvent.getCount(),
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

        if (eventRepository.findAvailableEvent() != null) {
            throw new CustomException(ExceptionMessage.EVENT_ALREADY_ONGOING);
        }

        Event event = Event.builder()
            .giftName(request.giftName())
            .giftPicture(request.giftPictureUrl())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .announcementDate(request.announcementDate())
            .count(request.count())
            .build();
        eventRepository.save(event);
    }
}

