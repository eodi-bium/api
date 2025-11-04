package com.eodi.bium.repository;

import com.eodi.bium.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByMemberId(String userId);
}
