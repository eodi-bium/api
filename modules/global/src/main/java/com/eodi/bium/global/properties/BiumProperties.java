package com.eodi.bium.global.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "bium")
public class BiumProperties {

    private final String frontendDeployUrl;
    private final String frontendDevUrl;
}
