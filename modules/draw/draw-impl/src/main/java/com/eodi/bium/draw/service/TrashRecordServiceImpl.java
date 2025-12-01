package com.eodi.bium.draw.service;

import com.eodi.bium.draw.api.TrashRecordService;
import com.eodi.bium.draw.dto.response.TrashRecordResponse;
import com.eodi.bium.draw.entity.TrashRecord;
import com.eodi.bium.draw.repsoitory.TrashRecordRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrashRecordServiceImpl implements TrashRecordService {

    private final TrashRecordRepository trashRecordRepository;

    @Override
    public List<TrashRecordResponse> getRecordsByMemberId(String memberId) {
        List<TrashRecord> entities = trashRecordRepository.findAllByMemberId(memberId);
        return entities.stream()
            .map(e -> new TrashRecordResponse(
                e.getRecyclingType(),
                e.getCount()))
            .toList();
    }

}
