package com.eodi.bium.review;


import com.eodi.bium.review.dto.SubmitReviewRequest;

public interface ReviewService {

    void submitReview(SubmitReviewRequest request);
}