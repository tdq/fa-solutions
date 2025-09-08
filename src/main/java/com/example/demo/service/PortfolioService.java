package com.example.demo.service;

import com.example.demo.service.dto.Transaction;
import jakarta.annotation.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * Portfolio service allows to get information about requested portfolio
 */
public interface PortfolioService {

    /**
     * Provides list of transactions from requested portfolio
     * @param portfolioId portfolio id for which to fetch transactions
     * @param startDate optional start date
     * @param endDate optional end date
     * @return reactive stream of {@link Transaction}
     */
    @PreAuthorize("@checkPortfolioAccess.canGetInformation(authentication, #portfolioId)")
    Flux<Transaction> getTransactions(long portfolioId, @Nullable LocalDate startDate, @Nullable LocalDate endDate);
}

