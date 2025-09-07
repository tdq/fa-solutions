package com.example.demo.service;

import com.example.demo.service.dto.Portfolio;
import jakarta.annotation.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Mono;

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
    Mono<Portfolio> getPortfolio(long portfolioId, @Nullable LocalDate startDate, @Nullable LocalDate endDate);
}
