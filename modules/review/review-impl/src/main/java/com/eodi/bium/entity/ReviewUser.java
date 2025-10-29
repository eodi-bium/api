package com.eodi.bium.entity;

import com.eodi.bium.common.error.CustomException;
import com.eodi.bium.common.error.ExceptionMessage;
import com.eodi.bium.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewUser {

    @Id
    @Column(name = "review_user_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

    private ReviewUser(Member member, Review review) {
        if (member == null || review == null) {
            log.error("member/review required");
            throw new CustomException(ExceptionMessage.INTERNAL_SERVER_ERROR);
        }
        this.member = member;
        this.review = review;
    }

    private static ReviewUser create(Member member, Review review) {
        return new ReviewUser(member, review);
    }
}
