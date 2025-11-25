package com.eodi.bium.review.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eodi.bium.review.dto.ReviewResponse;
import com.eodi.bium.review.dto.request.SubmitReviewRequest;
import com.eodi.bium.review.entity.Review;
import com.eodi.bium.review.repository.ReviewRepository;
import com.eodi.bium.review.service.ReviewServiceImpl;
import java.net.URI;
import java.util.List;
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
        String memberId = "1";
        SubmitReviewRequest req = new SubmitReviewRequest(100L, memberId, (short) 5,
            URI.create("https://x/y.jpg"), "굿");

        // when
        reviewService.submitReview(req);
        reviewService.submitReview(req);
        reviewService.submitReview(req);

        // then
        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review saved = captor.getValue();
        assertThat(saved.getMemberId()).isEqualTo(memberId);
        assertThat(saved.getPlaceId()).isEqualTo(100L);
    }

    @Test
    void 특정장소_리뷰_제출() {
        // given
        String memberId = "1";
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

    @Test
    void 특정장소_리뷰_조회() {
        // given
        Long placeId = 100L;
        ReviewResponse response = new ReviewResponse("nickname", "굿", (short) 5, "https://x.y/z.jpg");

        when(reviewRepository.findAllByPlaceId(placeId)).thenReturn(List.of(response));

        // when
        List<ReviewResponse> result = reviewService.getReviewsByPlaceId(placeId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(response);
    }
}
