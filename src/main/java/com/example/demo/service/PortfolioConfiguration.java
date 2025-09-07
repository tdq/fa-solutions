package com.example.demo.service;

import com.example.demo.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PortfolioConfiguration {

    @Bean
    PortfolioService portfolioService(Client client) {
        return new PortfolioServiceImpl(client);
    }

    @Bean
    CheckPortfolioAccess checkPortfolioAccess() {
        return new CheckPortfolioAccess();
    }
}
