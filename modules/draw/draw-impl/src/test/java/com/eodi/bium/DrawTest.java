package com.eodi.bium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import com.eodi.bium.draw.entity.DrawEvent;
import com.eodi.bium.draw.entity.DrawPoint;
import com.eodi.bium.draw.repsoitory.DrawRepository;
import com.eodi.bium.draw.repsoitory.EventRepository;
import com.eodi.bium.draw.service.DrawServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DrawTest {

    @InjectMocks
    DrawServiceImpl drawService;


    @InjectMocks
    EvnetServiceImpl eventService;

    @Mock
    DrawRepository drawRepository;

    @Mock
    RecordRepository recordRepository;

    @Mock
    EventRepository eventRepository;

    @Test
    public void 추첨을_시작한다() {
        //given
        Long eventId = 1L;
        DrawStartRequest request = new DrawStartRequest(eventId);

        //when
        drawService.startDraw(request);
        List<String> joinedMembers = drawService.getDrawJoinedMembers(eventId);
        DrawEvent drawEvent = eventService.getEventInfo(eventId);

        //then
        Assertions.assertTrue(joinedMembers.contains(drawEvent.getWinnerId()));
    }

    @Test
    public void 추첨에_참여한다() {
        //given
        String memberId = "KAKAO_123";
        Long recordId = 100L;
        Long eventId = 1L;
        Integer point = 5;
        DrawPointRequest request = new DrawPointRequest(recordId, eventId, memberId, point);

        //when
        drawService.joinDraw(request);

        //then
        ArgumentCaptor<DrawPoint> captor = ArgumentCaptor.forClass(DrawPoint.class);
        verify(drawRepository).save(captor.capture());

        DrawPoint saved = captor.getValue();
        assertThat(saved.getMemberId()).isEqualTo(memberId);
        assertThat(saved.getRecordId()).isEqualTo(recordId);
        assertThat(saved.getEventId()).isEqualTo(eventId);
        assertThat(saved.getPoint()).isEqualTo(point);
    }
}