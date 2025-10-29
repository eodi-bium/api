package com.eodi.bium.controller;

import com.eodi.bium.dto.SubmitReviewRequest;
import com.eodi.bium.review.api.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/review")
@RequiredArgsConstructor
public class ReviewController {

    ReviewService reviewService;

    @PostMapping("")
    private void submitReview(SubmitReviewRequest request) {
        reviewService.submitReview(request);
    }
}
