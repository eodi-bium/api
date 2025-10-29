package com.eodi.bium.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.net.URI;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private URI photoUrl;

    private Short star;

    private Review(URI photoUrl, Short star) {
        this.photoUrl = photoUrl;
        this.star = star;
    }

    public static Review create(URI photoUrl, Short star) {
        return new Review(photoUrl, star);
    }
}