package com.example.demo.client;

import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

class TrymeClientImpl implements Client {

    private final WebClient webClient;
    private final HttpGraphQlClient graphQlClient;
    private final TokenService tokenService;

    TrymeClientImpl(WebClient.Builder builder, TokenService tokenService, String basepath) {
        this.webClient = builder
                .baseUrl(basepath)
                .build();

        this.tokenService = tokenService;

        this.graphQlClient = HttpGraphQlClient.builder(webClient).build();
    }

    @Override
    public Mono<ClientGraphQlResponse> query(String query, Map<String, Object> variables) {
        return tokenService.getAccessToken()
                .flatMap(token -> this.graphQlClient
                        .mutate()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build()
                        .document(query)
                        .variables(variables)
                        .execute());
    }
}
