package com.eodi.bium.review.member.repository;

import com.eodi.bium.review.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    boolean existsByMemberId(String userId);
}
