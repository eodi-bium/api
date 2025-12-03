package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.EventService;
import com.eodi.bium.draw.dto.request.DrawEventAddRequest;
import com.eodi.bium.draw.dto.request.EventJoinRequest;
import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.draw.dto.response.EventResponse;
import com.eodi.bium.draw.dto.response.EventResponse.SingleEventResponse;
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

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    public EventResponse getEvents() {
        List<Event> events = eventRepository.findAllByOrderByIdDesc();
        if (events.isEmpty()) {
            throw new CustomException(ExceptionMessage.EVENT_NOT_FOUND);
        }
        List<SingleEventResponse> singleEventResponses = events.stream()
            .map(this::convertToSingleEventResponse)
            .toList();
        return new EventResponse(singleEventResponses);
    }

    private SingleEventResponse convertToSingleEventResponse(Event event) {
        EventStats eventStats = eventJoinRepository.getEventStatsByEventId(event.getId());
        return new SingleEventResponse(
            event.getId(),
            event.getGiftName(),
            event.getCount(),
            event.getGiftImageUrl(),
            new EventResponse.EventPeriod(
                event.getStartDate(),
                event.getEndDate(),
                event.getAnnouncementDate()
            ),
            new EventResponse.EventStats(
                eventStats.totalAccumulatedPoints(),
                eventStats.totalParticipants()
            ),
            event.getWinnerId()
        );
    }

    @Override
    public void addEvent(DrawEventAddRequest request) {
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
    public Slice<EventRecord> getEventWithMemberId(String memberId, Pageable pageable) {
        return eventJoinRepository.findEventRecordsByMemberId(memberId, pageable);
    }
}

