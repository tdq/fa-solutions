package com.example.demo.service;

import com.example.demo.service.dto.Transaction;
import jakarta.annotation.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 *
 */
public interface PortfolioService {

    /**
     *
     * @param portfolioId
     * @param startDate
     * @param endDate
     * @return
     */
    @PreAuthorize("@checkPortfolioAccess.canGetInformation(authentication, #portfolioId)")
    Flux<Transaction> getTransactions(long portfolioId, @Nullable LocalDate startDate, @Nullable LocalDate endDate);
}

