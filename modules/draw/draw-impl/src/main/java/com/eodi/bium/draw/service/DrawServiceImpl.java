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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final EventRepository eventRepository;
    private final EventJoinRepository eventJoinRepository;
    private final MemberPointRepository memberPointRepository;

    @Override
    @Transactional
    public DrawResultResponse startDraw(DrawStartRequest request) {
        Event event = eventRepository.findById(request.eventId()).orElseThrow(
            () -> new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR)
        );
        if (event.getWinnerId() != null) {
            throw new CustomException(ExceptionMessage.DRAW_ALREADY_COMPLETED);
        }
        List<DrawPointView> candidates = eventJoinRepository.findCandidatesByEventId(
            request.eventId());

        Long drawCount = event.getCount(); // 당첨자 인원
        List<String> winnerIds = new ArrayList<>(); // 당첨자들

        if (candidates.size() <= drawCount) { // 전원 당첨
            for (DrawPointView candidate : candidates) {
                winnerIds.add(candidate.memberId());
            }
            candidates.clear();
        } else { // 당첨 로직 수행
            for (int i = 0; i < drawCount; i++) {
                long totalWeight = candidates.stream().mapToLong(DrawPointView::point).sum();

                if (totalWeight <= 0) {
                    break;
                }

                double randomValue = Math.random() * totalWeight;
                int currentWeight = 0;

                for (int j = 0; j < candidates.size(); j++) {
                    DrawPointView item = candidates.get(j);
                    currentWeight += item.point();

                    if (randomValue < currentWeight) {
                        winnerIds.add(item.memberId());
                        candidates.remove(j); // 당첨자는 후보에서 제거 (중복 당첨 방지)
                        break;
                    }
                }
            }
        }

        // 당첨자가 한 명도 없는 경우 (참여자가 0명이었을 때 등)
        if (winnerIds.isEmpty()) {
            // 상황에 따라 예외를 던지거나, 빈 결과로 리턴할 수 있습니다.
            throw new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR);
        }

        String winnerIdString = String.join(",", winnerIds);
        System.out.println("당첨자 발생!!: " + winnerIdString);

        event.setWinnerId(winnerIdString);
        eventRepository.save(event);

        // 남은 사람들(candidates)에게만 포인트 환급 수행
        rollbackPoints(candidates);

        return new DrawResultResponse(winnerIdString);
    }

    private void rollbackPoints(List<DrawPointView> losers) {
        if (losers.isEmpty()) {
            return;
        }
        for (DrawPointView item : losers) {
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
