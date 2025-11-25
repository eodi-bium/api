package com.eodi.bium.review.dto.request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;

public record SubmitReviewRequest(
    @NotNull Long placeId,
    @NotNull String memberId,
    @NotNull Short star,
    URI photoUrl,
    @NotNull String content
) {

}
