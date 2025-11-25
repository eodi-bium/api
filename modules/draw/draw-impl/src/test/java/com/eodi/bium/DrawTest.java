package com.eodi.bium;

import static org.assertj.core.api.Assertions.assertThat;

import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import com.eodi.bium.draw.entity.DrawEvent;
import com.eodi.bium.draw.entity.DrawPoint;
import com.eodi.bium.draw.repsoitory.DrawEventRepository;
import com.eodi.bium.draw.repsoitory.DrawPointRepository;
import com.eodi.bium.draw.service.DrawServiceImpl;
import com.eodi.bium.draw.service.EventServiceImpl;
import com.eodi.bium.draw.view.DrawPointView;
import com.eodi.bium.review.error.CustomException;
import com.eodi.bium.review.error.ExceptionMessage;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DrawServiceImpl.class, EventServiceImpl.class})
class DrawTest {

    @Autowired
    DrawServiceImpl drawService;
    @Autowired
    EventServiceImpl eventService;
    @Autowired
    DrawPointRepository drawPointRepository;
    @Autowired
    DrawEventRepository drawEventRepository;

    @Test
    public void 추첨을_시작한다() {
        //given
        DrawEvent sampleEvent = DrawEvent.builder()
            .giftName("Sample Gift")
            .giftPicture("sample.jpg")
            .build();

        // 실제 DB에 저장하여 ID 생성
        DrawEvent savedEvent = drawEventRepository.save(sampleEvent);
        Long eventId = savedEvent.getId();

        for (int i = 0; i < 10; i++) {
            drawService.joinDraw(
                new DrawPointRequest((long) i, eventId, "KAKAO_" + i, 5)
            );
        }
        for (int i = 0; i < 5; i++) {
            drawService.joinDraw(
                new DrawPointRequest((long) i, eventId, "KAKAO_" + i, 10)
            );
        }

        //when
        drawService.startDraw(new DrawStartRequest(eventId));
        List<String> joinedMembers = drawService.getDrawJoinedMembers(eventId);
        DrawEvent drawEvent = drawEventRepository.findById(eventId)
            .orElseThrow(() -> new CustomException(ExceptionMessage.EVENT_NOT_FOUND));

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
        // 실제 저장된 데이터 조회
        List<DrawPointView> points = drawPointRepository.findByEventId(eventId);
        // findByEventId가 GroupBy 쿼리이므로 개별 엔티티 조회가 아닐 수 있음.
        // 단순히 findAll로 검증하거나 비즈니스 로직에 맞는 검증 수행

        // 저장 검증을 위해 findAll 사용
        List<DrawPoint> allPoints = drawPointRepository.findAll();
        assertThat(allPoints).hasSize(1);
        DrawPoint saved = allPoints.get(0);

        assertThat(saved.getMemberId()).isEqualTo(memberId);
        assertThat(saved.getRecordId()).isEqualTo(recordId);
        assertThat(saved.getEventId()).isEqualTo(eventId);
        assertThat(saved.getPoint()).isEqualTo(point);
    }

    @SpringBootApplication
    static class TestConfig {

    }
}
