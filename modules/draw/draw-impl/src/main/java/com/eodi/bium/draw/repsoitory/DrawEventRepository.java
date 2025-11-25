package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.DrawEvent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawEventRepository extends JpaRepository<DrawEvent, Long> {

    @Query("""
          SELECT de.id FROM DrawEvent de
          WHERE de.winnerId IS NULL
        """)
    List<Long> findAllAvailableEventIds();
}
