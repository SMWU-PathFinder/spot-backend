package com.pathfinder.spot.common.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);  // 비밀번호 설정

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder().build();
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    // RefreshToken 저장을 위한 redisTemplate 설정
    @Bean(name = "tokenRedisTemplate")
    public RedisTemplate<String, String> tokenRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> tokenRedisTemplate = new RedisTemplate<>();
        tokenRedisTemplate.setConnectionFactory(connectionFactory);
        tokenRedisTemplate.setKeySerializer(new StringRedisSerializer());
        tokenRedisTemplate.setValueSerializer(new StringRedisSerializer());
        return tokenRedisTemplate;
    }
}
