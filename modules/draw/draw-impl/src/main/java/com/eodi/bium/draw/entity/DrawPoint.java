package com.eodi.bium.draw.entity;

import com.eodi.bium.global.entity.CreatedAt;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "point", nullable = false)
    private Long point;

    // 단방향 OneToMany 설정
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
        name = "draw_point_trash_record_link", // 매핑 테이블 이름
        joinColumns = @JoinColumn(name = "draw_point_id"), // 현재 엔티티(Point)의 FK
        inverseJoinColumns = @JoinColumn(
            name = "trash_record_id",
            unique = true // ★ 중요: 하나의 Disposal은 하나의 Point에만 속하게 강제 (1:N 관계 보장)
        )
    )
    private List<TrashRecord> trashRecords = new ArrayList<>();

    @Builder
    public DrawPoint(String memberId, Long point, List<TrashRecord> trashRecords) {
        this.trashRecords = trashRecords;
        this.memberId = memberId;
        this.point = point;
    }
}