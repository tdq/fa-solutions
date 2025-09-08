package com.example.demo.service;

import org.springframework.security.core.Authentication;

/**
 * This class is done just for fun to demonstrate how ABAC can be used here
 */
class CheckPortfolioAccess {
    /**
     * Checks if authenticated user is able to get data by portfolioId
     * @param authentication
     * @param portfolioId
     * @return
     */
    public boolean canGetInformation(Authentication authentication, Long portfolioId) {
        final var user = authentication.getPrincipal();

        // In this case we are just allowing access
        return true;
    }
}
