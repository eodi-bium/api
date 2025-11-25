package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.DrawPoint;
import com.eodi.bium.draw.view.DrawPointView;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawPointRepository extends JpaRepository<DrawPoint, Long> {

    @Query("SELECT dp.memberId FROM DrawPoint dp WHERE dp.eventId = :eventId")
    List<String> getMembersWithEventId(Long eventId);

    @Query("""
            SELECT new com.eodi.bium.draw.view.DrawPointView(dp.memberId, SUM(dp.point))
            FROM DrawPoint dp
            GROUP BY dp.memberId
            HAVING SUM(dp.point) > 0
        """)
    List<DrawPointView> findByEventId(Long eventId);
}
