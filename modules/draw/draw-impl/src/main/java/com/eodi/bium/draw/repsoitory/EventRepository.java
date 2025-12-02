package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.Event;
import java.util.Optional;
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

    Event findTopByOrderByIdDesc();

    @Query("SELECT e.winnerId FROM Event e WHERE e.id = :eventId")
    String findWinnerIdById(Long eventId);
}
