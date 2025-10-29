package com.eodi.bium.controller;

import com.eodi.bium.ReviewService;
import com.eodi.bium.dto.SubmitReviewRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("")
    private void submitReview(@Valid SubmitReviewRequest request) {
        reviewService.submitReview(request);
    }
}
