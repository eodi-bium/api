package com.eodi.bium.draw.service;

import com.eodi.bium.draw.DrawService;
import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.entity.DrawPoint;
import com.eodi.bium.draw.repsoitory.DrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DrawServiceImpl implements DrawService {

    private final DrawRepository drawRepository;

    @Override
    @Transactional
    public void joinDraw(DrawPointRequest request) {
        DrawPoint drawPoint = DrawPoint.builder()
            .recordId(request.recordId())
            .eventId(request.eventId())
            .memberId(request.memberId())
            .point(request.point())
            .build();
            
        drawRepository.save(drawPoint);
    }
}

