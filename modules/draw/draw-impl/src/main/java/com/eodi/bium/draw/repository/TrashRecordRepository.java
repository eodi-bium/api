package com.eodi.bium.draw.repository;

import com.eodi.bium.draw.entity.TrashRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashRecordRepository extends JpaRepository<TrashRecord, Long> {

    List<TrashRecord> findAllByMemberId(String memberId);

}
