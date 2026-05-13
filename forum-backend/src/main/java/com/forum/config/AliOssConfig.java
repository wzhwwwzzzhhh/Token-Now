package com.forum.config;

import com.forum.properties.AliOssproperties;
import com.forum.utils.AliossUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AliOssConfig {


    /**
     * 阿里云OSS配置
     */
    @Bean
    public AliossUtil aliossUtil(AliOssproperties aliOssproperties) {
        log.info("初始化阿里云OSS配置");
        return new AliossUtil(aliOssproperties.getEndpoint(),
                aliOssproperties.getAccessKeyId(),
                aliOssproperties.getAccessKeySecret(),
                aliOssproperties.getBucketName());
    }
}
