package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.EventJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJoinRepository extends JpaRepository<EventJoin, Long> {

    @Query("""
            SELECT SUM(ej.point)
            FROM EventJoin ej
            WHERE ej.eventId = :eventId AND ej.memberId = :memberId
        """)
    Long getPointsByEventIdAndMemberId(Long eventId, String memberId);

    @Query("""
        SELECT new com.eodi.bium.draw.dto.view.EventStats(
          SUM(ej.point),
          COUNT(DISTINCT  ej.memberId))
        FROM EventJoin ej
        WHERE ej.eventId = :eventId
        """)
    com.eodi.bium.draw.dto.view.EventStats getEventStatsByEventId(Long eventId);
}
