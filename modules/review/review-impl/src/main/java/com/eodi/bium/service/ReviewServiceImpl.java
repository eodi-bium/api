package com.eodi.bium.service;

import com.eodi.bium.MemberService;
import com.eodi.bium.ReviewService;
import com.eodi.bium.dto.SubmitReviewRequest;
import com.eodi.bium.dto.Member.MemberResponse;
import com.eodi.bium.entity.Review;
import com.eodi.bium.repository.ReviewRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final MemberService memberService;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public void submitReview(SubmitReviewRequest request) {
        UUID memberId = request.memberId();
        Optional<MemberResponse> memberResponse = memberService.findMember(memberId);
        if (memberResponse.isEmpty()) {
            memberService.joinMember(memberId);
        }

        Long placeId = request.placeId();
        // TODO place 서비스 다녀오기

        Review review = Review.create(request.photoUrl(), request.start(),
            request.memberId(),
            placeId);
        reviewRepository.save(review);

    }
}
