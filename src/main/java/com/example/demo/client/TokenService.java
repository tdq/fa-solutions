package com.example.demo.client;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
class TokenService {
    private static final int EXPIRE_MARGIN_IN_SECONDS = 5;

    private final String username;
    private final String password;
    private final String basepath;

    private final WebClient webClient;
    private volatile TokenResponse tokenResponse;
    private volatile Instant tokenExpiry = Instant.EPOCH;
    private final AtomicReference<Mono<String>> ongoingFetchTokenRef = new AtomicReference<>();

    TokenService(WebClient.Builder builder, String username, String password, String basepath) {
        this.username = username;
        this.password = password;
        this.basepath = basepath;

        this.webClient = builder
                .baseUrl(basepath)
                .build();
    }

    /**
     *
     * @return
     */
    Mono<String> getAccessToken() {
        if (tokenResponse != null && Instant.now().isBefore(tokenExpiry)) {
            return Mono.just(tokenResponse.accessToken());
        }

        Mono<String> ongoing = ongoingFetchTokenRef.get();
        if (ongoing != null) {
            return ongoing;
        }

        Mono<String> fetchToken = (tokenResponse != null
                ? refreshToken(tokenResponse.refreshToken()).onErrorResume(e -> login())
                : login())
                .map(this::updateTokenAndReturn)
                .cache()
                .doFinally(sig -> ongoingFetchTokenRef.set(null));

        if (ongoingFetchTokenRef.compareAndSet(null, fetchToken)) {
            return fetchToken;
        } else {
            return ongoingFetchTokenRef.get();
        }
    }

    private Mono<TokenResponse> login() {
        return webClient.post().uri(basepath).body(BodyInserters.fromFormData("client_id", "external-api")
                        .with("username", username)
                        .with("password", password)
                        .with("grant_type", "password"))
                .retrieve()
                .bodyToMono(TokenResponse.class);

        // TODO process on login error
    }

    private Mono<TokenResponse> refreshToken(String refreshToken) {
        return webClient.post()
                .uri(basepath)
                .body(BodyInserters.fromFormData("client_id", "external-api")
                        .with("refresh_token", refreshToken)
                        .with("grant_type", "refresh_token"))
                .retrieve()
                .bodyToMono(TokenResponse.class);

        // TODO process on refresh error properly
    }

    private String updateTokenAndReturn(TokenResponse response) {
        tokenResponse = response;
        tokenExpiry = Instant.now().plusSeconds(response.expiresIn() - EXPIRE_MARGIN_IN_SECONDS);

        return response.accessToken();
    }
}
