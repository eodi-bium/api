package com.eodi.bium.draw.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
public class EventJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_join_id")
    private Long id;

    private String memberId;
    private Long point;
    private Long eventId;

    @Builder
    private EventJoin(
        String memberId,
        Long point,
        Long eventId
    ) {
        this.memberId = memberId;
        this.point = point;
        this.eventId = eventId;
    }
}
