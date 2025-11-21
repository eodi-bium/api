package com.eodi.bium.review.test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eodi.bium.review.dto.SubmitReviewRequest;
import com.eodi.bium.review.error.CustomException;
import com.eodi.bium.review.error.ExceptionMessage;
import com.eodi.bium.review.member.entity.Member;
import com.eodi.bium.review.member.repository.MemberRepository;
import com.eodi.bium.review.member.repository.ReviewRepository;
import com.eodi.bium.review.member.service.ReviewServiceImpl;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class ReviewServiceImplTest {

    @Container
    static MySQLContainer<?> db = new MySQLContainer<>("mysql:8.4")
        .withReuse(true);

    @Autowired
    ReviewServiceImpl reviewService;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    private MemberRepository memberRepository;

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", db::getJdbcUrl);
        r.add("spring.datasource.username", db::getUsername);
        r.add("spring.datasource.password", db::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Test
    @Transactional
    void 회원가입한_멤버는_리뷰_제출에_성공한다() {
        // given
        String memberId = "1";
        SubmitReviewRequest req = new SubmitReviewRequest(100L, memberId, (short) 5,
            URI.create("https://x/y.jpg"), "굿");
        memberRepository.save(
            Member.builder().memberId(memberId).role("USER").password("password").provider("KAKAO")
                .nickname("참새")
                .build());

        // when
        reviewService.submitReview(req);

        // then
        var saved = reviewRepository.findAll();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0).getMemberId()).isEqualTo(memberId);
        assertThat(saved.get(0).getPlaceId()).isEqualTo(100L);
    }

    @Test
    @Transactional
    void 회원가입하지않으면_리뷰_제출시_오류가_발생한다() {
        // given
        String memberId = "1";
        SubmitReviewRequest req = new SubmitReviewRequest(
            100L, memberId, (short) 5,
            URI.create("https://x/y.jpg"), "굿"
        );

        // when, then
        assertThatThrownBy(() -> reviewService.submitReview(req))
            .isInstanceOf(CustomException.class)
            .hasMessage(ExceptionMessage.NOT_ENOUGH_ACCESS.getMessage());
    }

    @SpringBootApplication(scanBasePackages = "com.eodi.bium")
    static class TestApp {

    }
}