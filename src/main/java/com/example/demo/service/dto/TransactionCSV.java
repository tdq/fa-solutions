package com.example.demo.service.dto;

public record TransactionCSV(
        String shortName,
        String securityName,
        String isinCode,
        String currencyCode,
        long amount,
        double unitPrice,
        long tradeAmount,
        String typeName,
        String transactionDate,
        String settlementDate) {
    public static TransactionCSV fromTransaction(String shortName, Transaction transaction) {
        return new TransactionCSV(
                shortName,
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
