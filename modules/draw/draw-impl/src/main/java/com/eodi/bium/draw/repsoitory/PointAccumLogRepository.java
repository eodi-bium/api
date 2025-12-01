package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.PointAccumLog;
import com.eodi.bium.draw.view.DrawPointView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointAccumLogRepository extends JpaRepository<PointAccumLog, Long> {

    @Query("""
            SELECT new com.eodi.bium.draw.view.DrawPointView(dp.memberId, COALESCE(SUM(dp.point),0))
            FROM PointAccumLog dp
            GROUP BY dp.memberId
            HAVING SUM(dp.point) > 0
        """)
    List<DrawPointView> findByEventId(Long eventId);
}
