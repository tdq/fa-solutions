package com.example.demo.service;

import com.example.demo.client.Client;
import com.example.demo.service.dto.Portfolio;
import jakarta.annotation.Nullable;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.client.ClientResponseField;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class PortfolioServiceImpl implements PortfolioService {
    private Client client;

    public PortfolioServiceImpl(Client client) {
        this.client = client;
    }

    @Override
    public Mono<Portfolio> getPortfolio(long portfolioId, @Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        final var query =
                """
                 query report($id: Long!, $from:String, $to: String) {
                    portfolio(id: $id) {
                        shortName,
                        transactions(startDate: $from, endDate: $to) {
                            securityName,
                            security {
                                isinCode
                            },
                            currencyCode,
                            amount,
                            unitPrice,
                            tradeAmount,
                            typeName,
                            transactionDate,
                            settlementDate
                        }
                    }
                 }
                """;

        final Map<String, Object> params = new HashMap<>();
        params.put("id", portfolioId);

        if(startDate != null) {
            params.put("from", startDate.toString());
        }

        if(endDate != null) {
            params.put("to", endDate.toString());
        }

        return client.query(query, params).flatMap(response -> {
            if(!response.getErrors().isEmpty()) {
                return Mono.error(new RuntimeException(response.getErrors().stream().map(ResponseError::getMessage).collect(Collectors.joining())));
            }

            ClientResponseField field = response.field("portfolio");

            return Mono.justOrEmpty(field.toEntity(Portfolio.class));
        });
    }
}
