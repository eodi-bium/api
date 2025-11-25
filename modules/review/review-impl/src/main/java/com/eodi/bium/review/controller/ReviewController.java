package com.eodi.bium.review.controller;

import com.eodi.bium.review.ReviewService;
import com.eodi.bium.review.annotation.AuthUserId;
import com.eodi.bium.review.dto.SubmitReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    public void submitReview(
        @AuthUserId Long memberId,
        @RequestBody SubmitReviewRequest request
    ) {
        SubmitReviewRequest secureRequest = new SubmitReviewRequest(
            request.placeId(),
            memberId,
            request.star(),
            request.photoUrl(),
            request.content()
        );

        reviewService.submitReview(secureRequest);
    }
}
