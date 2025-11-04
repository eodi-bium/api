package com.eodi.bium.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import com.eodi.bium.ReviewService;
import com.eodi.bium.dto.SubmitReviewRequest;
import com.eodi.bium.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = ReviewServiceImplTest.TestApp.class)
@Testcontainers
class ReviewServiceImplTest {

    @Container
    static MySQLContainer<?> db = new MySQLContainer<>("mysql:8.4")
        .withReuse(true);

    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewRepository reviewRepository;

    @DynamicPropertySource
    static void datasourceProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", db::getJdbcUrl);
        r.add("spring.datasource.username", db::getUsername);
        r.add("spring.datasource.password", db::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Test
    @Transactional
    void 리뷰제출_회원가입안한_멤버() {
        // given
        var memberId = java.util.UUID.randomUUID();
        var req = new SubmitReviewRequest(100L, memberId, (short) 5,
            java.net.URI.create("https://x/y.jpg"), "굿");

        // when
        reviewService.submitReview(req);

        // then: DB에 실제로 저장되었는지 확인
        var saved = reviewRepository.findAll();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0).getMemberId()).isEqualTo(memberId);
        assertThat(saved.get(0).getPlaceId()).isEqualTo(100L);
    }

    @Test
    @Transactional
    void 리뷰제출_회원가입한_멤버() {
        // given
        var memberId = java.util.UUID.randomUUID();
        var req = new SubmitReviewRequest(100L, memberId, (short) 5,
            java.net.URI.create("https://x/y.jpg"), "굿");

        // when
        reviewService.submitReview(req);

        // then: DB에 실제로 저장되었는지 확인
        var saved = reviewRepository.findAll();
        assertThat(saved).hasSize(1);
        assertThat(saved.get(0).getMemberId()).isEqualTo(memberId);
        assertThat(saved.get(0).getPlaceId()).isEqualTo(100L);
    }

    @SpringBootApplication(scanBasePackages = "com.eodi.bium")
    @EnableJpaRepositories(basePackages = "com.eodi.bium")
    @EntityScan(basePackages = "com.eodi.bium")
    static class TestApp {

    }
}