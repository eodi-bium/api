package com.eodi.bium.review.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record SubmitReviewRequest(
    @NotNull Long placeId,
    @NotNull Long memberId,
    @NotNull Short star,
    URI photoUrl,
    @NotNull String content
) {

}
