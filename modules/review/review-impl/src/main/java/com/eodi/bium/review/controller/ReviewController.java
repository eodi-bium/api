package com.eodi.bium.review.controller;

import com.eodi.bium.global.annotation.AuthUserId;
import com.eodi.bium.review.api.ReviewService;
import com.eodi.bium.review.dto.ReviewResponse;
import com.eodi.bium.review.dto.request.SubmitReviewRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/submit")
    public void submitReview(
        @AuthUserId String memberId,
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

    @GetMapping("/{id}")
    public List<ReviewResponse> getReviews(
        @PathVariable Long id
    ) {
        return reviewService.getReviewsByPlaceId(id);
    }
}
