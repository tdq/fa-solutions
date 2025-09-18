package com.example.demo.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionCSV(
        @JsonProperty("Portfolio's shortName")
        String portfolioShortName,
        @JsonProperty("Security name")
        String securityName,
        @JsonProperty("Security ISIN code")
        String isinCode,
        @JsonProperty("Transaction's currency code")
        String currencyCode,
        @JsonProperty("Amount")
        long amount,
        @JsonProperty("Unit price")
        double unitPrice,
        @JsonProperty("Trade amount")
        long tradeAmount,
        @JsonProperty("Type name")
        String typeName,
        @JsonProperty("Transaction date")
        String transactionDate,
        @JsonProperty("Settlement date")
        String settlementDate) {
    public static TransactionCSV fromTransaction(Transaction transaction) {
        return new TransactionCSV(
                transaction.portfolioShortName(),
                transaction.securityName(),
                transaction.security() != null ? transaction.security().isinCode() : "",
                transaction.currencyCode(),
                transaction.amount(),
                transaction.unitPrice(),
                transaction.tradeAmount(),
                transaction.typeName(),
                transaction.transactionDate() != null ? transaction.transactionDate().toString() : "",
                transaction.settlementDate() != null ? transaction.settlementDate().toString() : "");
    }
}
