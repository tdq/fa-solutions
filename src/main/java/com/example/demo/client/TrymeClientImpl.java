package com.example.demo.client;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Service
class TrymeClientImpl implements Client {
    private static final Logger log = LoggerFactory.getLogger(TrymeClientImpl.class);

    private final WebClient webClient;
    private final HttpGraphQlClient graphQlClient;
    private final TokenService tokenService;

    TrymeClientImpl(
            WebClient.Builder builder,
            TokenService tokenService,
            @Value("${tryme.graphql.path}") String basepath) {
        this.webClient = builder
                .baseUrl(basepath)
                .build();

        this.tokenService = tokenService;

        this.graphQlClient = HttpGraphQlClient.builder(webClient).build();
    }

    @Override
    public Mono<ClientGraphQlResponse> query(@Nonnull String query, Map<String, Object> variables) {
        Objects.requireNonNull(query, "Please provide query");

        return tokenService.getAccessToken()
                .flatMap(token -> this.graphQlClient
                        .mutate()
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .build()
                        .document(query)
                        .variables(variables)
                        .execute()).onErrorResume(error -> {
                            log.error("Error on GraphQL client", error);

                            return Mono.error(error);
                });
    }
}
