package com.eodi.bium.review.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.eodi.bium.review.dto.SubmitReviewRequest;
import com.eodi.bium.review.entity.Review;
import com.eodi.bium.review.member.repository.ReviewRepository;
import com.eodi.bium.review.member.service.ReviewServiceImpl;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @InjectMocks
    ReviewServiceImpl reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Test
    void 리뷰_제출() {
        // given
        Long memberId = 1L;
        SubmitReviewRequest req = new SubmitReviewRequest(100L, memberId, (short) 5,
            URI.create("https://x/y.jpg"), "굿");

        // when
        reviewService.submitReview(req);

        // then
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review saved = captor.getValue();
        assertThat(saved.getMemberId()).isEqualTo(memberId);
        assertThat(saved.getPlaceId()).isEqualTo(100L);
    }
}
