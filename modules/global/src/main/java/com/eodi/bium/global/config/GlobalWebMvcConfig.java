package com.eodi.bium.global.config;

import com.eodi.bium.global.properties.BiumProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class GlobalWebMvcConfig implements WebMvcConfigurer {

    private final BiumProperties biumProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(biumProperties.getFrontendDevUrl(),
                biumProperties.getFrontendDeployUrl())
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
