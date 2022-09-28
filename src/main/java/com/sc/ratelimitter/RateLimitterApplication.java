package com.sc.ratelimitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sc.ratelimitter.*")
@ConfigurationPropertiesScan(
		basePackages = {"com.sc.ratelimitter.properties.OpenApiConfigProperties",
		"com.sc.ratelimitter.jwt.properties.JwtConfigurationProperties"}
)
public class RateLimitterApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitterApplication.class, args);
	}

}
