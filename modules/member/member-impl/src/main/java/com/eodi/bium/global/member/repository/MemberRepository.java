package com.eodi.bium.global.member.repository;

import com.eodi.bium.global.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByMemberId(String userId);
}
