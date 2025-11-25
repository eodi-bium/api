package com.eodi.bium.review.dto;

public record ReviewResponse(
    String memberNickname,
    String content,
    Short star,
    String photoUrl
) {

}

