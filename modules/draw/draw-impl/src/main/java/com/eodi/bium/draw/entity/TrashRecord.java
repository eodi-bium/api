package com.eodi.bium.draw.entity;

import com.eodi.bium.global.enums.RecyclingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자 보호
public class TrashRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Increment
    @Column(name = "trash_record_id")
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private Long count;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RecyclingType recyclingType;

    // 객체 생성을 위한 빌더 패턴 적용
    @Builder
    public TrashRecord(Long count, String memberId, RecyclingType recyclingType) {
        this.count = count;
        this.memberId = memberId;
        this.recyclingType = recyclingType;
    }
}