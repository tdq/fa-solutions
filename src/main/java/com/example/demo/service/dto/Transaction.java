package com.example.demo.service.dto;

import java.time.LocalDate;

public record Transaction(
        String securityName,
        Security security,
        String currencyCode,
        long amount,
        double unitPrice,
        long tradeAmount,
        String typeName,
        LocalDate transactionDate,
        LocalDate settlementDate) {
}
