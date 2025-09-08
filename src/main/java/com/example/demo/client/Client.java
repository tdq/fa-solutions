package com.example.demo.client;

import org.springframework.graphql.client.ClientGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Client interface for accessing GraphQL
 */
public interface Client {
    /**
     * Execute query on GraphQL
     * @param query String query
     * @param variables map of variables for query
     * @return reactive response
     */
    Mono<ClientGraphQlResponse> query(String query, Map<String, Object> variables);
}
