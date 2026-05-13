package com.forum.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "forum.jwt")
public class JwtProperties {
    private String secretKey;
    private long ttl;
    private String tokenName;
}
