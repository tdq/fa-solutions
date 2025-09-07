package com.example.demo.service;

import org.springframework.security.core.Authentication;

class CheckPortfolioAccess {
    public boolean canGetInformation(Authentication authentication, Long portfolioId) {
        final var user = authentication.getPrincipal();

        // In this case we are just allowing access
        return true;
    }
}
