package com.eodi.bium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan("com.eodi.bium")
public class BiumApplication {

    public static void main(String[] args) {
        SpringApplication.run(BiumApplication.class, args);
    }
}
