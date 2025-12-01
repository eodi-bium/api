package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.request.EventJoinRequest;
import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.dto.response.MyPointResponse;
import com.eodi.bium.draw.dto.response.UserEventStatusResponse;
import com.eodi.bium.draw.dto.view.EventStats;
import com.eodi.bium.draw.entity.Event;
import com.eodi.bium.draw.entity.EventJoin;
import com.eodi.bium.draw.entity.MemberPoint;
import com.eodi.bium.draw.repsoitory.EventJoinRepository;
import com.eodi.bium.draw.repsoitory.EventRepository;
import com.eodi.bium.draw.repsoitory.MemberPointRepository;
import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventJoinRepository eventJoinRepository;
    private final MemberPointRepository memberPointRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void joinEvent(String memberId, EventJoinRequest request) {
        MemberPoint memberPoint = memberPointRepository.findByMemberId(memberId).orElseThrow(
            () -> new CustomException(ExceptionMessage.USER_NOT_FOUND)
        );
        if (memberPoint.getPoint() < request.point()) {
            throw new CustomException(ExceptionMessage.INSUFFICIENT_POINTS);
        }
        memberPoint.setPoint(memberPoint.getPoint() - request.point());
        memberPointRepository.save(memberPoint);
        eventJoinRepository.save(EventJoin.builder().eventId(
            request.eventId()).memberId(memberId).point(request.point()).build(
        ));
    }

    @Override
    public UserEventStatusResponse getMyEventStatus(String userId, Long eventId) {
        Long totalPoint = eventJoinRepository.getPointsByEventIdAndMemberId(eventId, userId);
        EventStats eventStats = eventJoinRepository.getEventStatsByEventId(eventId);
        return new UserEventStatusResponse(
            totalPoint,
            (double) Math.round(
                (double) totalPoint / eventStats.totalAccumulatedPoints() * 100 * 10000)
                / 10000
        );
    }

    @Override
    public MyPointResponse getMyPoint(String memberId) {
        MemberPoint memberPoint = memberPointRepository.findByMemberId(memberId).orElseThrow(
            () -> new CustomException(ExceptionMessage.USER_NOT_FOUND)
        );
        return new MyPointResponse(memberPoint.getPoint());
    }

    @Override
    public EventResponse getEvent() {
        Event availableEvent = eventRepository.findAvailableEvent();
        if (availableEvent == null) {
            throw new CustomException(ExceptionMessage.EVENT_NOT_FOUND);
        }
        EventStats eventStats = eventJoinRepository.getEventStatsByEventId(availableEvent.getId());
        return new EventResponse(
            availableEvent.getId(),
            availableEvent.getGiftName(),
            availableEvent.getCount(),
            availableEvent.getGiftImageUrl(),
            new EventResponse.EventPeriod(
                availableEvent.getStartDate(),
                availableEvent.getEndDate(),
                availableEvent.getAnnouncementDate()
            ),
            new EventResponse.EventStats(
                eventStats.totalAccumulatedPoints(),
                eventStats.totalParticipants()
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
            .giftImageUrl(request.giftImageUrl())
            .startDate(request.startDate())
            .endDate(request.endDate())
            .announcementDate(request.announcementDate())
            .count(request.count())
            .build();
        eventRepository.save(event);
    }

    @Override
    public List<EventRecord> getEventWithMemberId(String memberId) {
        return eventJoinRepository.findEventRecordsByMemberId(memberId);
    }
}

