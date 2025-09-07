package com.example.demo.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
class ClientConfiguration {

    @Value("${tryme.username}")
    private String username;

    @Value("${tryme.password}")
    private String password;

    @Value("${tryme.auth.path}")
    private String authpath;

    @Value("${tryme.graphql.path}")
    private String graphqlPath;

    @Bean
    Client client(WebClient.Builder builder, TokenService tokenService) {
        return new TrymeClientImpl(builder, tokenService, graphqlPath);
    }

    @Bean
    TokenService tokenService(WebClient.Builder builder) {
        return new TokenService(builder, username, password, authpath);
    }
}
