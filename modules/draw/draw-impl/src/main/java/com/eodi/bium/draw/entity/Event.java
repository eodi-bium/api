package com.eodi.bium.draw.entity;

import com.eodi.bium.global.entity.CreatedAt;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends CreatedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String giftName;

    private Long count;

    @Column(columnDefinition = "TEXT")
    private String giftPicture;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime announcementDate;
    @Setter
    private String winnerId;

    @Builder
    private Event(String giftName, String giftPicture,
        LocalDateTime startDate, LocalDateTime endDate,
        LocalDateTime announcementDate,
        Long count) {
        this.count = count;
        this.giftName = giftName;
        this.giftPicture = giftPicture;
        this.startDate = startDate;
        this.endDate = endDate;
        this.announcementDate = announcementDate;
    }
}
