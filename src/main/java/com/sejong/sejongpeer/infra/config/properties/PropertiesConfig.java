package com.sejong.sejongpeer.infra.config.properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({S3Properties.class})
@Configuration
public class PropertiesConfig {
}
