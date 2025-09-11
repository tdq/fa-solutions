package com.example.demo.service;

import com.example.demo.client.Client;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is done just for fun to demonstrate how ABAC can be used here
 */
class CheckPortfolioAccess {
    private final Client client;

    public CheckPortfolioAccess(Client client) {
        this.client = client;
    }

    /**
     * Checks if authenticated user is able to get data by portfolioId
     * @param authentication
     * @param portfolioId
     * @return
     */
    public boolean canGetInformation(Authentication authentication, Long portfolioId) {
        final var user = authentication.getPrincipal();

        final var query =
                """
                 query report($id: Long!) {
                    portfolio(id: $id) {
                        id
                    }
                 }
                """;

        final Map<String, Object> params = new HashMap<>();
        params.put("id", portfolioId);

        // In this case we are just allowing access
        return Boolean.TRUE.equals(client.query(query, params)
                .map(response -> response.getErrors().isEmpty())
                .block());
    }
}
