package com.eodi.bium.draw.api;

import com.eodi.bium.draw.dto.response.TrashRecordResponse;
import java.util.List;

public interface TrashRecordService {

    List<TrashRecordResponse> getRecordsByMemberId(String memberId);

}
