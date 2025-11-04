package com.eodi.bium.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotNull(message = "memberId가 null입니다.")
    private String memberId;
    @NotNull(message = "password가 null입니다.")
    private String password;
}
