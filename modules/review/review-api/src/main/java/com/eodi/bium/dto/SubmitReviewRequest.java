package com.eodi.bium.dto;

import java.util.UUID;
import lombok.Getter;

@Getter
public class SubmitReviewRequest {

    private UUID memberId;
    private Short start;
    private String photoUrl;
    private String content;
}
