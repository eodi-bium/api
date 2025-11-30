package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.EventJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJoinRepository extends JpaRepository<EventJoin, Long> {

}
