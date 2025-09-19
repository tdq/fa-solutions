package com.example.demo.service;

import com.example.demo.client.Client;
import com.example.demo.service.dto.Transaction;
import jakarta.annotation.Nullable;
import org.springframework.graphql.ResponseError;
import org.springframework.graphql.client.ClientResponseField;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class PortfolioServiceImpl implements PortfolioService {
    private final Client client;

    public PortfolioServiceImpl(Client client) {
        this.client = client;
    }

    @Override
    public Flux<Transaction> getTransactions(long portfolioId, @Nullable LocalDate startDate, @Nullable LocalDate endDate) {
        if(startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("From must be before or the same date as To");
        } else if((startDate != null && endDate == null) || (endDate != null && startDate == null)) {
            throw new IllegalArgumentException("Both or none of date ranges must be specified");
        }

        final var query =
                """
                 query report($id: Long!, $from:String, $to: String) {
                    transactions(portfolioId: $id, startDate: $from, endDate: $to) {
                        portfolioShortName,
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
                """;

        final Map<String, Object> params = new HashMap<>();
        params.put("id", portfolioId);

        if(startDate != null) {
            params.put("from", startDate.toString());
        }

        if(endDate != null) {
            params.put("to", endDate.toString());
        }

        return client.query(query, params).flatMapMany(response -> {
            if(!response.getErrors().isEmpty()) {
                return Mono.error(new RuntimeException(response.getErrors().stream().map(ResponseError::getMessage).collect(Collectors.joining())));
            }

            ClientResponseField field = response.field("transactions");

            return Flux.fromIterable(field.toEntityList(Transaction.class));
        });
    }
}
