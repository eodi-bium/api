package com.eodi.bium.draw.repsoitory;

import com.eodi.bium.draw.entity.MemberPoint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPointRepository extends JpaRepository<MemberPoint, Long> {

    Optional<MemberPoint> findByMemberId(String memberId);
}
