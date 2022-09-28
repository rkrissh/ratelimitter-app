package com.sc.ratelimitter.util.jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "com.sc.ratelimitter.util")
@Data
public class JwtConfigurationProperties {

    private String secretKey;

}