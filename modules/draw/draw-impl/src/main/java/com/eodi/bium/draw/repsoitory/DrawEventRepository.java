package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.DrawEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawEventRepository extends JpaRepository<DrawEvent, Long> {

    @Query("""
          SELECT d
            FROM DrawEvent d
          WHERE d.winnerId IS NULL
        """)
    DrawEvent findAvailableEvent();
}
