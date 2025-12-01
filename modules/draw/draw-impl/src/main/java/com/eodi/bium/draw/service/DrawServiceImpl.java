package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.DrawService;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import com.eodi.bium.draw.dto.response.DrawResultResponse;
import com.eodi.bium.draw.entity.Event;
import com.eodi.bium.draw.entity.MemberPoint;
import com.eodi.bium.draw.repsoitory.EventJoinRepository;
import com.eodi.bium.draw.repsoitory.EventRepository;
import com.eodi.bium.draw.repsoitory.MemberPointRepository;
import com.eodi.bium.draw.repsoitory.PointAccumLogRepository;
import com.eodi.bium.draw.view.DrawPointView;
import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final EventRepository eventRepository;
    private final PointAccumLogRepository pointAccumLogRepository;
    private final EventJoinRepository eventJoinRepository;
    private final MemberPointRepository memberPointRepository;

    @Override
    public DrawResultResponse startDraw(DrawStartRequest request) {
        Event event = eventRepository.findById(request.eventId()).orElseThrow(
            () -> new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR)
        );
        if (event.getWinnerId() != null) {
            throw new CustomException(ExceptionMessage.DRAW_ALREADY_COMPLETED);
        }
        List<DrawPointView> candidates = eventJoinRepository.findCandidatesByEventId(
            request.eventId());
        int totalWeight = 0;
        for (DrawPointView item : candidates) {
            totalWeight += item.point();
        }

        // 만약 포인트 합이 0 이하라면 뽑을 수 없음 (예외처리)
        if (totalWeight <= 0) {
            throw new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR);
        }

        // 2. 0 ~ totalWeight 사이의 랜덤 값 생성
        // (double을 사용하여 정밀도를 높이거나, 단순히 Random().nextInt(totalWeight)를 써도 됨)
        double randomValue = Math.random() * totalWeight;

        // 3. 누적 합을 계산하며 당첨자 찾기
        int currentWeight = 0;
        for (DrawPointView item : candidates) {
            currentWeight += item.point();

            // 현재 아이템의 구간에 랜덤값이 포함되는지 확인
            if (randomValue < currentWeight) {
                event = eventRepository.findById(request.eventId()).orElseThrow(
                    () -> new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR)
                );
                System.out.println("당첨자 발생!!: " + item.memberId());
                event.setWinnerId(item.memberId());
                eventRepository.save(event);
                rollbackPoints(request.eventId(), item.memberId());
                return new DrawResultResponse(item.memberId());
            }
        }

        throw new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR);
    }

    private void rollbackPoints(Long eventId, String memberId) {
        List<DrawPointView> notWinners = eventJoinRepository.findLosersByEventId(eventId, memberId);
        for (DrawPointView item : notWinners) {
            Optional<MemberPoint> byMemberId = memberPointRepository.findByMemberId(
                item.memberId());
            if (byMemberId.isPresent()) {
                MemberPoint memberPoint = byMemberId.get();
                memberPoint.setPoint(memberPoint.getPoint() + item.point());
                memberPointRepository.save(memberPoint);
            }
        }
    }
}
