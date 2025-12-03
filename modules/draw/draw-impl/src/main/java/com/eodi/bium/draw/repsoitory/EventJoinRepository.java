package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.dto.response.EventRecord;
import com.eodi.bium.draw.entity.EventJoin;
import com.eodi.bium.draw.view.DrawPointView;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJoinRepository extends JpaRepository<EventJoin, Long> {

    @Query("""
            SELECT COALESCE(SUM(ej.point), 0)
            FROM EventJoin ej
            WHERE ej.eventId = :eventId AND ej.memberId = :memberId
        """)
    Long getPointsByEventIdAndMemberId(Long eventId, String memberId);

    @Query("""
        SELECT new com.eodi.bium.draw.dto.view.EventStats(
          COALESCE(SUM(ej.point), 0),
          COUNT(DISTINCT  ej.memberId))
        FROM EventJoin ej
        WHERE ej.eventId = :eventId
        """)
    com.eodi.bium.draw.dto.view.EventStats getEventStatsByEventId(Long eventId);

    @Query("""
            SELECT new com.eodi.bium.draw.dto.response.EventRecord(
                e.giftName,
                e.count,
                e.startDate,
                e.endDate,
                e.announcementDate,
                COALESCE(SUM(ej.point),0)
            )
            FROM EventJoin ej
            JOIN Event e ON ej.eventId = e.id
            WHERE ej.memberId = :memberId
            GROUP BY e.id
            ORDER BY MAX(ej.id) DESC
        """)
    Slice<EventRecord> findEventRecordsByMemberId(String memberId, Pageable pageable);

    @Query("""
        SELECT new com.eodi.bium.draw.view.DrawPointView(
            ej.memberId,
            COALESCE(SUM(ej.point), 0)
        )
        FROM EventJoin ej
        WHERE ej.eventId = :eventId
        GROUP BY ej.memberId
        """)
    List<DrawPointView> findCandidatesByEventId(Long eventId);
}
