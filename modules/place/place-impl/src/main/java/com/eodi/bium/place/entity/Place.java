package com.eodi.bium.place.entity;

import com.eodi.bium.place.enums.RecyclingType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;
import org.springframework.data.geo.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;

    @Column(name = "place_name")
    private String name;

    private Double latitude;

    private Double longitude;

    private String address;

    private String phoneNumber;

    @ElementCollection(targetClass = RecyclingType.class)
    @CollectionTable(name = "place_recycling_type", joinColumns = @JoinColumn(name = "placeId"))
    @Enumerated(EnumType.STRING)
    private Set<RecyclingType> recyclingTypes = new HashSet<>();

}