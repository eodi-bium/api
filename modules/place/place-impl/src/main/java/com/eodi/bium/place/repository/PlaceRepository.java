package com.eodi.bium.place.repository;

import com.eodi.bium.place.entity.Place;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    @Query(value = """
        SELECT * FROM place p
        WHERE ST_Distance_Sphere(
            POINT(p.longitude, p.latitude), 
            POINT(:longitude, :latitude)
        ) <= :radius
        """, nativeQuery = true)
    List<Place> findPlaceWithRadius(
        @Param("latitude") double latitude,
        @Param("longitude") double longitude,
        @Param("radius") double radius
    );
}
