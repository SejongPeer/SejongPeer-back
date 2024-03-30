package com.sejong.sejongpeer.infra.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public record S3Properties(String accessKey, String secretKey, String region, String bucket) {
}
