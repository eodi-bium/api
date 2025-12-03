package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.response.TrashRecordResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TrashRecordService {

    List<TrashRecordResponse> getRecordsByMemberId(String memberId);

}
