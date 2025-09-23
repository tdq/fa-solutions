package com.example.demo.service;

import com.example.demo.client.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @Mock
    private Client client;

    @Test
    public void test_checkSetToDateBeforeFromDateThrowsException() {
        final var service = new PortfolioServiceImpl(client);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getTransactions(3, LocalDate.now(), LocalDate.now().minusDays(1)));
    }

    @Test
    public void test_checkSetToDateFromDateNotSetThrowsException() {
        final var service = new PortfolioServiceImpl(client);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getTransactions(3, null, LocalDate.now()));
    }

    @Test
    public void test_checkToDateNotSetSetFromDateThrowsException() {
        final var service = new PortfolioServiceImpl(client);

        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getTransactions(3, LocalDate.now(), null));
    }
}
