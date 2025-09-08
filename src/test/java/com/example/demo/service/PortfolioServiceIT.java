package com.example.demo.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.util.stream.Collectors;

@SpringBootTest
public class PortfolioServiceIT {

    @Autowired
    private PortfolioService service;

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    public void test_fetchTransactionsPortfolio3ReturnsTransactions() {
        final var transactions = service.getTransactions(3, null, null).collect(Collectors.toList()).block();

        Assertions.assertNotNull(transactions);
        Assertions.assertFalse(transactions.isEmpty(), "Transactions are empty");
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    public void test_fetchTransactionsPortfolio3OneYearReturnsTransactions() {
        final var transactions = service.getTransactions(3, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31)).collect(Collectors.toList()).block();

        Assertions.assertNotNull(transactions);
        Assertions.assertFalse(transactions.isEmpty(), "Transactions are empty");
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    public void test_fetchTransactionsPortfolio3FutureDateReturnsEmptyList() {
        final var transactions = service.getTransactions(3, LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)).collect(Collectors.toList()).block();

        Assertions.assertNotNull(transactions);
        Assertions.assertTrue(transactions.isEmpty(), "Transactions are not empty");
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    public void test_fetchTransactionsPortfolio1ThrowsException() {
        Assertions.assertThrows(RuntimeException.class, () -> service.getTransactions(1, null, null).collect(Collectors.toList()).block());
    }

    @Test
    public void test_fetchTransactionsPortfolio3NoUserThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getTransactions(3, null, null).collect(Collectors.toList()).block());
    }
}
