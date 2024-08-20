package com.sejong.sejongpeer.infra.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {

	private String host;
	private int port;
}
