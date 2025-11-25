package com.eodi.bium.draw.entity;

import com.eodi.bium.review.entity.CreatedAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "draw_point") // 테이블 이름 예시
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DrawPoint extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "draw_point_id")
    private Long id;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "point", nullable = false)
    private Long point;

    @Builder
    public DrawPoint(Long eventId, String memberId, Long point, Long recordId) {
        this.recordId = recordId;
        this.eventId = eventId;
        this.memberId = memberId;
        this.point = point;
    }
}