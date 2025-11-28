package com.eodi.bium.review.api;


import com.eodi.bium.review.dto.ReviewResponse;
import com.eodi.bium.review.dto.request.SubmitReviewRequest;
import java.util.List;

public interface ReviewService {

    void submitReview(SubmitReviewRequest request);

    List<ReviewResponse> getReviewsByPlaceId(Long placeId);
}
