package com.eodi.bium.member.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    public long accessExpirationTime;
    public String secret;
    public long refreshExpirationTime;
    private String frontEndDeployUrl;

    public void setAccessExpirationTime(long seconds) {
        accessExpirationTime = seconds * 1000; // ms 변경
    }
}
