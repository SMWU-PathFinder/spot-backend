package com.pathfinder.spot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ApiClientConfig {

    @Bean
    public WebClient kakaoWebClient() {
        return WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public KakaoLoginApiClient kakaoApiClient(WebClient kakaoWebClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(kakaoWebClient))
                .build();
        return factory.createClient(KakaoLoginApiClient.class);
    }
}