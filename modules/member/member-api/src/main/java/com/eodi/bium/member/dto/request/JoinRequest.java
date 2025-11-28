package com.eodi.bium.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JoinRequest {

    @NotNull
    private String id;
    @NotNull
    private String password;
}
