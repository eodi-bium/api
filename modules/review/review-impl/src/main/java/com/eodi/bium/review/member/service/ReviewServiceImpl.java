package com.eodi.bium.review.member.service;

import com.eodi.bium.review.ReviewService;
import com.eodi.bium.review.dto.SubmitReviewRequest;
import com.eodi.bium.review.entity.Review;
import com.eodi.bium.review.member.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;

    @Override
    public void submitReview(SubmitReviewRequest request) {
        Review review = Review.create(
            request.photoUrl(),
            request.star(),
            request.memberId(),
            request.placeId()
        );

        reviewRepository.save(review);
    }
}
