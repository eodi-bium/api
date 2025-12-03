package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.PointService;
import com.eodi.bium.draw.dto.request.DrawPointRequest;
import com.eodi.bium.draw.dto.request.DrawPointRequest.TypeAndCount;
import com.eodi.bium.draw.entity.MemberPoint;
import com.eodi.bium.draw.entity.PointAccumLog;
import com.eodi.bium.draw.entity.TrashRecord;
import com.eodi.bium.draw.repository.MemberPointRepository;
import com.eodi.bium.draw.repository.PointAccumLogRepository;
import com.eodi.bium.draw.repository.TrashRecordRepository;
import com.eodi.bium.member.api.MemberService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final TrashRecordRepository trashRecordRepository;
    private final MemberPointRepository memberPointRepository;
    private final PointAccumLogRepository pointAccumLogRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public void addPoint(DrawPointRequest request) {
        memberService.findMember(request.memberId());
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

        PointAccumLog pointAccumLog = PointAccumLog.builder()
            .eventId(request.eventId())
            .memberId(request.memberId())
            .point((long) pont)
            .trashRecords(trashRecords)
            .build();
        pointAccumLogRepository.save(pointAccumLog);

        Optional<MemberPoint> byMemberId = memberPointRepository.findByMemberId(request.memberId());
        if (byMemberId.isPresent()) {
            MemberPoint memberPoint = byMemberId.get();
            memberPoint.setPoint(memberPoint.getPoint() + pont);
            memberPointRepository.save(memberPoint);
        } else {
            MemberPoint memberPoint = MemberPoint.builder()
                .memberId(request.memberId())
                .point((long) pont)
                .build();
            memberPointRepository.save(memberPoint);
        }
    }

//    @Override
//    public List<String> getDrawJoinedMembers(Long eventId) {
//        return drawPointRepository.getMembersWithEventId(eventId);
//    }
}
