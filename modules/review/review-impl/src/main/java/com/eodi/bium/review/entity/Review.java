package com.eodi.bium.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.net.URI;
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

    private String photoUrl;

    private Short star;


    private Long placeId;

    private Long memberId;

    private Review(String photoUrl, Short star, Long memberId, Long placeId) {
        this.photoUrl = photoUrl;
        this.star = star;
        this.memberId = memberId;
        this.placeId = placeId;
    }

    public static Review create(URI photoUrl, Short star, Long memberId, Long placeId) {
        return new Review(photoUrl.toString(), star, memberId, placeId);
    }
}