package com.eodi.bium.global.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotNull
    private String id;
    @NotNull
    private String password;
}
