package com.eodi.bium.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.UUID;

public record SubmitReviewRequest(
    @NotNull Long placeId,
    @NotNull UUID memberId,
    @NotNull Short start,
    URI photoUrl,
    @NotNull String content
) {

}
