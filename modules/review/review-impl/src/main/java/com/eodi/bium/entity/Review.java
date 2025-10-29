package com.eodi.bium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.net.URI;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private URI photoUrl;

    private Short star;

    private Long placeId;

    private UUID memberId;

    private Review(URI photoUrl, Short star, UUID memberId, Long placeId) {
        this.photoUrl = photoUrl;
        this.star = star;
        this.memberId = memberId;
        this.placeId = placeId;
    }

    public static Review create(URI photoUrl, Short star, UUID memberId, Long placeId) {
        return new Review(photoUrl, star, memberId, placeId);
    }
}