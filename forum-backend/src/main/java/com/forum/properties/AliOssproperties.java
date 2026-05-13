package com.forum.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "forum.alioss")
@Data
public class AliOssproperties {

    private  String endpoint;
    private  String accessKeyId;
    private  String accessKeySecret;
    private  String bucketName;
}
