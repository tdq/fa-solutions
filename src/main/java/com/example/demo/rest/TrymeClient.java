package com.example.demo.rest;

import org.springframework.graphql.client.ClientGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 *
 */
public interface TrymeClient {
    /**
     *
     * @param query
     * @param variables
     * @return
     */
    Mono<ClientGraphQlResponse> query(String query, Map<String, Object> variables);
}
