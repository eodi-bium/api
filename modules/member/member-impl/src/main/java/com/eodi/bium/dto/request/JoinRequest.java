package com.eodi.bium.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class JoinRequest {

    @NotNull
    private String id;
    @NotNull
    private String password;
}
