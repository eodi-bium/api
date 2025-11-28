package com.eodi.bium.place.repository;

import com.eodi.bium.place.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(value = """
        SELECT p.* FROM place p
        INNER JOIN place_recycling_type prt ON p.place_id = prt.place_id
        WHERE prt.recycling_types = :recyclingType
        AND ST_Distance_Sphere(
            POINT(p.longitude, p.latitude), 
            POINT(:longitude, :latitude)
        ) <= :radius
        """, nativeQuery = true)
    List<Place> findPlace(
        @Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("radius") double radius,
        @Param("recyclingType") String recyclingType
    );
}