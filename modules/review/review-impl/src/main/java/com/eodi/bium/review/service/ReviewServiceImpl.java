package com.eodi.bium.review.service;

import com.eodi.bium.member.api.MemberService;
import com.eodi.bium.review.api.ReviewService;
import com.eodi.bium.review.dto.ReviewResponse;
import com.eodi.bium.review.dto.request.SubmitReviewRequest;
import com.eodi.bium.review.entity.Review;
import com.eodi.bium.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;

    @Override
    @Transactional
    public void submitReview(SubmitReviewRequest request) {
        Review review = Review.create(
            request.photoUrl(),
            request.star(),
            request.memberId(),
            request.content(),
            request.placeId()
        );

        reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByPlaceId(Long placeId) {
        return reviewRepository.findAllByPlaceId(placeId);
    }
}
