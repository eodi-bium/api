package com.eodi.bium.draw.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MemberPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_point_id")
    private long id;

    @Column(name = "member_id")
    private String memberId;

    @Column(name = "point")
    @Setter
    private Long point;

    @Builder
    private MemberPoint(
        String memberId,
        Long point
    ) {
        this.memberId = memberId;
        this.point = point;
    }
}
