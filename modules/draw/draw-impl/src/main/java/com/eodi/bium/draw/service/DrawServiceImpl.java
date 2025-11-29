package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.DrawService;
import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawPointRequest.TypeAndCount;
import com.eodi.bium.draw.dto.request.DrawStartRequest;
import com.eodi.bium.draw.dto.response.DrawResultResponse;
import com.eodi.bium.draw.entity.DrawEvent;
import com.eodi.bium.draw.entity.DrawPoint;
import com.eodi.bium.draw.entity.TrashRecord;
import com.eodi.bium.draw.repsoitory.DrawEventRepository;
import com.eodi.bium.draw.repsoitory.DrawPointRepository;
import com.eodi.bium.draw.repsoitory.TrashRecordRepository;
import com.eodi.bium.draw.view.DrawPointView;
import com.eodi.bium.global.error.CustomException;
import com.eodi.bium.global.error.ExceptionMessage;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final TrashRecordRepository trashRecordRepository;
    private final DrawEventRepository drawEventRepository;
    private final DrawPointRepository drawPointRepository;

    @Override
    @Transactional
    public void joinDraw(DrawPointRequest request) {
        List<TrashRecord> trashRecords = new ArrayList<>();
        for (TypeAndCount item : request.typeAndCounts()) {
            TrashRecord trashRecord = TrashRecord.builder()
                .memberId(request.memberId())
                .recyclingType(item.recyclingType())
                .count(item.count())
                .build();

            trashRecords.add(trashRecord);
        }
        trashRecordRepository.saveAll(trashRecords);

        int pont = 0;
        for (TypeAndCount item : request.typeAndCounts()) {
            pont += item.recyclingType().getPoint() * item.count();
        }

        DrawPoint drawPoint = DrawPoint.builder()
            .eventId(request.eventId())
            .memberId(request.memberId())
            .point((long) pont)
            .trashRecords(trashRecords)
            .build();
        drawPointRepository.save(drawPoint);
    }

    @Override
    public DrawResultResponse startDraw(DrawStartRequest request) {
        DrawEvent drawEvent = drawEventRepository.findById(request.eventId()).orElseThrow(
            () -> new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR)
        );
        if (drawEvent.getWinnerId() != null) {
            throw new CustomException(ExceptionMessage.DRAW_ALREADY_COMPLETED);
        }
        List<DrawPointView> candidates = drawPointRepository.findByEventId(request.eventId());
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
                drawEvent = drawEventRepository.findById(request.eventId()).orElseThrow(
                    () -> new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR)
                );
                System.out.println("당첨자 발생!!: " + item.memberId());
                drawEvent.setWinnerId(item.memberId());
                drawEventRepository.save(drawEvent);
                return new DrawResultResponse(item.memberId());
            }
        }

        throw new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR);
    }

    @Override
    public List<String> getDrawJoinedMembers(Long eventId) {
        return drawPointRepository.getMembersWithEventId(eventId);
    }
}
