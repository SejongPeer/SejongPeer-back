package com.sejong.sejongpeer.global.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sejong.sejongpeer.infra.config.properties.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories
public class RedisConfig {

	private final RedisProperties redisProperties;

	@Autowired
	public ObjectMapper objectMapper;

	@Bean
	public RedisConnectionFactory redisConnectionFactory(){
		return new LettuceConnectionFactory(redisProperties.host(), redisProperties.port());
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());

		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}
