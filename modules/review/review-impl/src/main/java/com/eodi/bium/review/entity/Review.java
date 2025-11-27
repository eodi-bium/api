package com.eodi.bium.review.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.net.URI;
import lombok.AccessLevel;
import lombok.Builder;
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

    private String content;

    private String memberId;

    private Review(String photoUrl, Short star, String memberId, String content, Long placeId) {
        this.photoUrl = photoUrl;
        this.star = star;
        this.memberId = memberId;
        this.content = content;
        this.placeId = placeId;
    }

    @Builder
    public static Review create(URI photoUrl, Short star, String memberId, String content,
        Long placeId) {
        return new Review(photoUrl.toString(), star, memberId, content, placeId);
    }
}