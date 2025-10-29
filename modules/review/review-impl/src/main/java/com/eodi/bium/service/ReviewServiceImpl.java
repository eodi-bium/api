package com.eodi.bium.service;

import com.eodi.bium.MemberService;
import com.eodi.bium.ReviewService;
import com.eodi.bium.repository.ReviewRepository;
import com.eodi.bium.dto.SubmitReviewRequest;
import com.eodi.bium.entity.ReviewUser;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class ReviewServiceImpl implements ReviewService {

    MemberService member;
    ReviewRepository reviewRepository;

    @Override
    public void submitReview(SubmitReviewRequest request) {
        UUID memberId= request.getMemberId();

        ReviewUser reviewUser =new ReviewUser(member, review)
        reviewRepository.save()
    }
}
