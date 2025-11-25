package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.DrawPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawRepository extends JpaRepository<DrawPoint, Long> {

}
