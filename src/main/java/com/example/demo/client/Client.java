package com.example.demo.client;

import org.springframework.graphql.client.ClientGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 *
 */
public interface Client {
    /**
     *
     * @param query
     * @param variables
     * @return
     */
    Mono<ClientGraphQlResponse> query(String query, Map<String, Object> variables);
}
