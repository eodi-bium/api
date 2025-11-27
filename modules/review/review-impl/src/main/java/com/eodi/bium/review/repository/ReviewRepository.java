package com.eodi.bium.review.repository;

import com.eodi.bium.review.dto.ReviewResponse;
import com.eodi.bium.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT new com.eodi.bium.review.dto.ReviewResponse(m.nickname, r.content, r.star, r.photoUrl)  FROM Review r JOIN Member m ON r.memberId = m.memberId WHERE r.placeId = :placeId")
    List<ReviewResponse> findAllByPlaceId(Long placeId);
}
