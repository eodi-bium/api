package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
          SELECT d
            FROM Event d
          WHERE d.winnerId IS NULL
        """)
    Event findAvailableEvent();
}
